package com.mycomp.demo.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    List<User> users = userServiceObj.getAllUsers();
    if (users != null) {
        return new ResponseEntity<>(users, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
    }
}


@GetMapping("/al")
public List<User> getAlProduct() {
    List<User> users = userServiceObj.getAllUsers();

    return users;
}

/*
* To do: use Optionnal<E>
 */
@GetMapping("/byid")
public ResponseEntity<User> getUser(@RequestParam(name = "id") String id) {
    User user = userServiceObj.getUser(id);
    if (user != null) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
    }
}

@DeleteMapping("/delete")
public void deleteUser(@RequestParam(name = "id") String id) {
    userServiceObj.deleteUser(id);
}
}
