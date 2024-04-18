/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycomp.demo.service;

import java.util.List;

import com.mycomp.demo.persistence.entity.User;

/**
 *
 * @author Amol
 */
public interface UserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public List<User> getAllUsers();

    List<User> getUsers(List<Long> userIds);

    public User getUser(Long id);

    void deleteUser(Long id);

    void deleteUsers(List<Long> idd);

}
