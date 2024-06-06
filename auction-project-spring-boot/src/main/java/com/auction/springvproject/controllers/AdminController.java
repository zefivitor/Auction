package com.auction.springvproject.controllers;


import com.auction.springvproject.payload.response.Users;
import com.auction.springvproject.security.services.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminServices adminServices;


    @GetMapping("/listusers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAllProducts() {
        return adminServices.listUsers();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        adminServices.deleteUser(id);
        return "User deleted successfully";
    }
}
