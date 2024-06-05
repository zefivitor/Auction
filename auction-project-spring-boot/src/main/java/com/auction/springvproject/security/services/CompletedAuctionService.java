package com.auction.springvproject.security.services;

import com.auction.springvproject.models.CompletedAuction;
import com.auction.springvproject.repository.CompletedAuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompletedAuctionService {
    @Autowired
    private CompletedAuctionRepository completedAuctionRepository;

    public List<CompletedAuction> getAllAuctionCompleted() {
        return completedAuctionRepository.findAll();
    }
}
