package com.mycomp.demo.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycomp.demo.persistence.entity.User;
import com.mycomp.demo.service.UserService;


/**
 *
 * @author Amol
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("user")
public class UserController {
@Autowired
private UserService userServiceObj;
Logger logger = LoggerFactory.getLogger(UserController.class);

@PostMapping("/save")
public ResponseEntity<User> save(@RequestBody User user) {
    User tempuser = userServiceObj.saveUser(user);
    if (tempuser != null) {
        return new ResponseEntity<>(tempuser, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(tempuser, HttpStatus.NOT_FOUND);
    }
}

@PutMapping("/update")
public ResponseEntity<User> update(@RequestBody User user) {
    User tempuser = userServiceObj.updateUser(user);
    if (tempuser != null) {
        return new ResponseEntity<>(tempuser, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(tempuser, HttpStatus.NOT_FOUND);
    }
}

@GetMapping("/all")
public ResponseEntity<List<User>> getAllProduct() {
    logger.info("Get all");
    List<User> users = userServiceObj.getAllUsers();
    if (users != null) {
        return new ResponseEntity<>(users, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
    }
}


@GetMapping("/all")
public List<User> getAllUsers() {
    List<User> users = userServiceObj.getAllUsers();

    return users;
}

@GetMapping()
public ResponseEntity<List<User>> getUsers(@RequestParam(name = "ids") List<Long> ids) {
    List<User> users = userServiceObj.getUsers(ids);
    if (users != null) {
        return new ResponseEntity<>(users, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
    }
}

@GetMapping("/id/{id}")
public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
    User user = userServiceObj.getUser(id);
    if (user != null) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
    }
}

@DeleteMapping("/id/{id}")
public void deleteUser(@PathVariable(name = "id") Long id) {
    userServiceObj.deleteUser(id);
}

@DeleteMapping()
public void deleteUsers(@RequestParam(name = "ids") List<Long> ids) {
    userServiceObj.deleteUsers(ids);
}
}
