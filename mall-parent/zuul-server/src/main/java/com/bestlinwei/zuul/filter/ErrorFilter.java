package com.bestlinwei.zuul.filter;

import com.netflix.zuul.ZuulFilter;

public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "err";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        return null;
    }
}
