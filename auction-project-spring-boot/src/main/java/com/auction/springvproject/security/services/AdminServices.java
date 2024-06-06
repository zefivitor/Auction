package com.auction.springvproject.security.services;

import com.auction.springvproject.models.User;
import com.auction.springvproject.dtopayload.response.Users;
import com.auction.springvproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServices {
    @Autowired
    private UserRepository userRepository;

    public List<Users> listUsers() {
        List<User> allUsers = userRepository.findAll();
        List<Users> users = new ArrayList<>();
        for (User u : allUsers) {
            Users user = new Users(u.getId(), u.getUsername(), u.getEmail(), u.getRoles());
            users.add(user);
        }
        return users;
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() + "  " + e.getMessage());
        }
    }
}
