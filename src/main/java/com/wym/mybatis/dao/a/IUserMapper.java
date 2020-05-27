package com.wym.mybatis.dao.a;

import com.wym.mybatis.model.User;

import java.util.List;

public interface IUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> getList();
}