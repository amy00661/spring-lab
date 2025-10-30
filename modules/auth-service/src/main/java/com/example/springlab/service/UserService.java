package com.example.springlab.service;

import com.example.springlab.model.UserEntity;
import com.example.springlab.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findByUsername(String username)  {
        return userRepository.findByUsername(username).orElse(null);
    }
}
