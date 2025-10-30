package com.example.springlab.service;

import com.example.springlab.model.RoleEntity;
import com.example.springlab.model.UserEntity;
import com.example.springlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final UserRepository userRepository;

    public List<RoleEntity> listRoleByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return user.getRoles();
    }
}
