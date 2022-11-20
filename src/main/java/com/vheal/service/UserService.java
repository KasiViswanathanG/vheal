package com.vheal.service;

import com.vheal.entity.User;

import java.util.List;

//Service layer for Drug entity
public interface UserService {

    public List<User> findAll();

    public User findById(int theId);

    public void save(User theUser);

    public void deleteById(int theId);

    public User findByEmail(String theEmail);
}
