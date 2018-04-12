package com.bestlinwei.account.controller;

import java.util.List;

import com.bestlinwei.account.service.IAccountService;
import com.bestlinwei.common.bean.account.AccAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	IAccountService accountService;

	@RequestMapping("list/{accountTid}")
	public List<AccAddress> getList(@RequestParam(required = true) @PathVariable("accountTid") String accountTid, String jwtToken) {
		List<AccAddress> list = accountService.getAddressList(accountTid);
		return list;
	}
}
