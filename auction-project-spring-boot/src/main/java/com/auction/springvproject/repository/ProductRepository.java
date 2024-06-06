package com.auction.springvproject.repository;

import com.auction.springvproject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.dateClosed < CURRENT_TIMESTAMP")
    List<Product> findExpiredProducts();

    @Query("SELECT p FROM Product p WHERE p.dateClosed > CURRENT_TIMESTAMP ORDER BY p.dateClosed ASC")
    List<Product> listProductByTime();

    @Query("SELECT SUM(p.price) FROM Product p WHERE p.buyerUser = :buyerUser")
    Double findTotalSpentByBuyer(@Param("buyerUser") String buyerUser);
}
