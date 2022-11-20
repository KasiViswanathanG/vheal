package com.vheal.service;

import com.vheal.dao.UserRepository;
import com.vheal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Implementation for Drug service
@Service
public class UserImpl implements UserService{

    private UserRepository userRepository;

    // constructor dependency injection
    @Autowired
    public UserImpl(UserRepository theUserRepository){
        userRepository = theUserRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {
        Optional<User> result = userRepository.findById(theId);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
        }
        else {
            // we didn't find the drug return null
            return theUser;
        }
        return theUser;
    }

    @Override
    public void save(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public User findByEmail(String theEmail) {
        return userRepository.findByEmail(theEmail);
    }
}
