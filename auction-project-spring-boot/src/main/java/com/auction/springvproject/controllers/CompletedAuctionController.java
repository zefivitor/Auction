package com.auction.springvproject.controllers;


import com.auction.springvproject.models.CompletedAuction;
import com.auction.springvproject.models.Product;
import com.auction.springvproject.payload.request.ProductRequest;
import com.auction.springvproject.security.services.CompletedAuctionService;
import com.auction.springvproject.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/completedauction")
public class CompletedAuctionController {

    private static final Logger logger = LoggerFactory.getLogger(CompletedAuctionController.class);


    @Autowired
    private CompletedAuctionService completedAuctionService;

    @GetMapping("/listall")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CompletedAuction> getAllCompletedAuction() {
        logger.info("Trying to get all completed auctions");
        return completedAuctionService.getAllAuctionCompleted();

    }

}
