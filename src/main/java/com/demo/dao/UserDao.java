/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.User;

/**
 *
 * @author Amol
 */
public interface UserDao extends JpaRepository<User, String> {

}
