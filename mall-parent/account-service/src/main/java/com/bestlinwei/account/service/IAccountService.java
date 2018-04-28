package com.bestlinwei.account.service;

import com.bestlinwei.common.bean.account.Account;

import java.util.List;


public interface IAccountService {

	/**
	 * @param phone
	 * @param password
	 * @return
	 */
	Account login(String phone, String password);
	
	/**
	 * @param phone
	 * @param password
	 * @return
	 */
	boolean signup(String phone, String password);

}
