/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycomp.demo.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycomp.demo.persistence.entity.User;

/**
 *
 * @author Amol
 */
public interface UserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public User getUser(String id);

    public JSONObject getUserFormated(String id);

    void deleteUser(String id);
    
    public JSONObject populateUserJson(User user) throws JSONException;
    
    public JSONObject populatUserJson(User user, JSONObject detailsJson) throws JSONException;
}
