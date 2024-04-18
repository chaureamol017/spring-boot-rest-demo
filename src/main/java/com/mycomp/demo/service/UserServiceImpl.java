/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycomp.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycomp.demo.persistence.entity.User;
import com.mycomp.demo.repository.UserDao;

/**
 *
 * @author Amol
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDaoObj;
    
//    @Autowired
//    private JpaService persistenceService;

    @Override
    public User saveUser(User user) {
        return userDaoObj.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userDaoObj.saveAndFlush(user);
    }

    @Override
    public List<User> getAllUsers() {
//    	List<User> allUsers = persistenceService.findAll(User.class);
//        return allUsers;
        return userDaoObj.findAll();
    }

    @Override
    public List<User> getUsers(final List<Long> userIds) {
//    	List<User> allUsers = persistenceService.findAll(User.class);
//        return allUsers;
        return userDaoObj.findAllById(userIds);
    }

    @Override
    public User getUser(Long id) {
        return userDaoObj.getOne(id);
    }

    @Override
    public void deleteUser(Long id) {
        userDaoObj.deleteById(id);
    }

    @Override
    public void deleteUsers(List<Long> ids) {
        ids.stream().forEach(id -> userDaoObj.deleteById(id));
    }

}
