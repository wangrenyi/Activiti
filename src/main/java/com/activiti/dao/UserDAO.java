package com.activiti.dao;

import org.springframework.stereotype.Repository;

import com.activiti.mapper.UserMapper;
import com.activiti.mybatis.BaseDAO;
import com.activiti.table.User;
import com.activiti.table.UserExample;

@Repository
public class UserDAO extends BaseDAO<User, UserExample, UserMapper> {

}
