package com.auction.springvproject.controllers;

import com.auction.springvproject.models.Product;
import com.auction.springvproject.payload.request.BuyRequest;
import com.auction.springvproject.payload.request.ProductRequest;
import com.auction.springvproject.security.services.ProductService;
import com.auction.springvproject.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableScheduling
@RequestMapping("/api/product")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Scheduled(fixedRate = 500000)
    public String test() {
        System.out.println("hellovitor");
        return "hellovitor";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String addProduct(@RequestBody ProductRequest productRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = new Product(userDetails.getUsername(), productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());
        productService.addProduct(product);

        return "Product added successfully ";
    }


    @PutMapping("/buyProduct")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String buyProductFromAuctions(@RequestBody BuyRequest buyRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductId(buyRequest.getId());
        if (!product.getCreatedUser().equals(userDetails.getUsername())) {


        }

        return "Product added successfully ";
    }


}
