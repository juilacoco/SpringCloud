package com.bestlinwei.zuul.filter;

import com.bestlinwei.common.util.JedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * token过滤器
 * “pre”过滤器，这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息、
 *  对参数进行一些设置等。对请求进行一个预处理
 */
@Component
public class AccessTokenFilter extends ZuulFilter {

    /**
     * (non-Javadoc) pre：路由之前 routing：路由之时 post： 路由之后 error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("token验证========");
        //token校验
        String auth = request.getHeader("Authorization");
        String accessToken = auth.split(" ")[1];
        ctx.set("LimitAccess", Boolean.TRUE);
        try {
            //解析token
            //Triple<Long, String, Long> clientTriple = databaseUserDetailService.loadClientByToken(accessToken);
            String userId = "";
            //Long intervalInMills = clientTriple.getLeft();
            Long intervalInMills = 0L;
            //Long limits = clientTriple.getRight();
            Long limits = 0L;
            if (intervalInMills != null && intervalInMills != 0l && limits != null && limits != 0l) {
                if (!access(userId, intervalInMills, limits)) {
                    ctx.set("LimitAccess", Boolean.FALSE);
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                    ctx.setResponseBody("The times of usage is limited");
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    public synchronized boolean access(String userId, long intervalInMills, long limit) {
        String key = genKey(userId, intervalInMills, limit);
        double intervalPerPermit = intervalInMills * 1.0 / limit;
        try (Jedis jedis = JedisUtil.getInstance().getJedis();) {
            Map<String, String> counter = jedis.hgetAll(key);
            if (counter.size() == 0) {
                TokenBucket tokenBucket = new TokenBucket(System.currentTimeMillis(), limit - 1);
                jedis.hmset(key, tokenBucket.toHash());
                return true;
            } else {
                TokenBucket tokenBucket = TokenBucket.fromHash(counter);
                long lastRefillTime = tokenBucket.getLastRefillTime();
                long refillTime = System.currentTimeMillis();
                long intervalSinceLast = refillTime - lastRefillTime;
                long currentTokensRemaining;
                if (intervalSinceLast > intervalInMills) {
                    currentTokensRemaining = limit;
                } else {
                    long grantedTokens = (long) (intervalSinceLast / intervalPerPermit);
                    currentTokensRemaining = Math.min(grantedTokens + tokenBucket.getTokensRemaining(), limit);
                }
                assert currentTokensRemaining >= 0;
                if (currentTokensRemaining == 0) {
                    tokenBucket.setTokensRemaining(currentTokensRemaining);
                    jedis.hmset(key, tokenBucket.toHash());
                    return false;
                } else {
                    tokenBucket.setLastRefillTime(refillTime);
                    tokenBucket.setTokensRemaining(currentTokensRemaining - 1);
                    jedis.hmset(key, tokenBucket.toHash());
                    return true;
                }
            }
        }
    }

    private String genKey(String userId, long intervalInMills, long limit) {
        return "rate:limiter:" + intervalInMills + ":" + limit + ":" + userId;
    }

    private static class TokenBucket {

        private long lastRefillTime;
        private long tokensRemaining;

        public TokenBucket(long lastRefillTime, long tokensRemaining){
            this.lastRefillTime = lastRefillTime;
            this.tokensRemaining = tokensRemaining;
        }

        public long getTokensRemaining() {
            return this.tokensRemaining;
        }

        public void setTokensRemaining(long tokensRemaining) {
            this.tokensRemaining = tokensRemaining;
        }

        public long getLastRefillTime() {
            return this.lastRefillTime;
        }

        public void setLastRefillTime(long lastRefillTime) {
            this.lastRefillTime = lastRefillTime;
        }

        public Map<String, String> toHash() {
            Map<String, String> hash = new HashMap<>();
            hash.put("lastRefillTime", String.valueOf(lastRefillTime));
            hash.put("tokensRemaining", String.valueOf(tokensRemaining));
            return hash;
        }

        public static TokenBucket fromHash(Map<String, String> hash) {
            long lastRefillTime = Long.parseLong(hash.get("lastRefillTime"));
            int tokensRemaining = Integer.parseInt(hash.get("tokensRemaining"));
            return new TokenBucket(lastRefillTime, tokensRemaining);
        }
    }
}
