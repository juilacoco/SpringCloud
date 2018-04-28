package com.bestlinwei.account.controller;

import com.bestlinwei.account.service.IAccountService;
import com.bestlinwei.common.bean.account.Account;
import com.bestlinwei.common.constant.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人账户
 * 
 * @author LINWEI
 *
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	IAccountService accountService;

	@Autowired
	DiscoveryClient discoveryClient;    //自动注入DiscoveryClinet

	@RequestMapping(value = "/hello" , method = RequestMethod.GET)
	public String sayHello(){

		ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();    //获得service实例

		System.out.println("/hello , host:"+serviceInstance.getHost()+"/n"+"/service_id:"+serviceInstance.getServiceId());

		return "成功访问服务";
	}

	/**
	 * 登陆
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "login", method = {RequestMethod.GET,RequestMethod.POST})
	public Account login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
		Account result = accountService.login(phone, password);
		return result;
	}

	/**
	 * 注册
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "signup", method = RequestMethod.GET)
	public String signup(String phone, String password) {
		boolean result = accountService.signup(phone, password);
		return result ? SystemConstants.Code.SUCCESS : SystemConstants.Code.FAIL;
	}
}
