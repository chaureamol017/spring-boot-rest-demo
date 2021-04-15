/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycomp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycomp.demo.persistence.entity.User;

/**
 *
 * @author Amol
 */
public interface UserDao extends JpaRepository<User, String> {

}
