package com.activiti.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activiti.common.PagingQuery;
import com.activiti.common.PagingResult;
import com.activiti.dao.UserDAO;
import com.activiti.table.User;
import com.activiti.table.UserExample;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public User saveUser(User user) {
        user.setCreateTime(new Date());
        user.setCreateUser("test");
        user.setUpdateTime(new Date());
        user.setUpdateUser("test");
        user.setStatus(1);
        user.setVersion(1);
        userDAO.insertSelective(user);
        return user;
    }

    @Transactional
    public User updateUser(User user) {
        user.setUpdateTime(new Date());
        user.setUpdateUser("test");
        this.userDAO.updateByPrimaryKeySelective(user);
        return user;
    }

    public List<User> getUserList() {
        return this.userDAO.selectByExample(null);
    }

    public PagingResult searchUsersQuery(PagingQuery pagingQuery) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        // Optional.ofNullable(pagingQuery.getCriteria()).map(mapper -> {
        // mapper.forEach((key, value) -> {
        // mapper.put(key, "%" + value + "%");
        // });
        // return mapper;
        // }).ifPresent(consumer -> criteria.andAnyLike(consumer));

        return this.userDAO.pagingByExample2(example, pagingQuery.getPageIndex(), pagingQuery.getPageSize());
    }

    public int deleteUser(Integer id) {
        return this.userDAO.deleteByPrimaryKey(id);
    }
}
