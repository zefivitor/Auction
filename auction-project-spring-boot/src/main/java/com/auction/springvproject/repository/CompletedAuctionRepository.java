package com.auction.springvproject.repository;

import com.auction.springvproject.models.BankAccount;
import com.auction.springvproject.models.CompletedAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedAuctionRepository extends JpaRepository<CompletedAuction, Long> {

}
