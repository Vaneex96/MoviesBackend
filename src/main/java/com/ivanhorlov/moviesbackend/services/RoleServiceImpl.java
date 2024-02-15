package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.entities.Role;
import com.ivanhorlov.moviesbackend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        Role role = null;
        Optional<Role> roleOptional = roleRepository.findRoleByName(name);

        if(roleOptional.isPresent()){
            role = roleOptional.get();
        }

        return role;
    }
}
