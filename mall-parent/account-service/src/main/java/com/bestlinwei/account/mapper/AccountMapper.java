package com.bestlinwei.account.mapper;

import com.bestlinwei.common.bean.account.Account;
import com.bestlinwei.common.bean.account.AccountCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {
    long countByExample(AccountCriteria example);

    int deleteByExample(AccountCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountCriteria example);

    Account selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountCriteria example);

    int updateByExample(@Param("record") Account record, @Param("example") AccountCriteria example);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}