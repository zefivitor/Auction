package com.auction.springvproject.controllers;

import com.auction.springvproject.models.BankAccount;
import com.auction.springvproject.models.CompletedAuction;
import com.auction.springvproject.models.Product;
import com.auction.springvproject.payload.request.BuyRequest;
import com.auction.springvproject.payload.request.ProductRequest;
import com.auction.springvproject.payload.response.MessageResponse;
import com.auction.springvproject.repository.BankAccountRepository;
import com.auction.springvproject.repository.CompletedAuctionRepository;
import com.auction.springvproject.repository.ProductRepository;
import com.auction.springvproject.security.services.ProductService;
import com.auction.springvproject.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableScheduling
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private CompletedAuctionRepository completedAuctionRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 10000000)
    public void findExpiredProducts() {

        logger.info("Looking to find expired products");
        List<Product> expiredProducts = productRepository.findExpiredProducts();
        for (Product product : expiredProducts) {
            CompletedAuction completedAuction = new CompletedAuction(product.getCreatedUser(),
                    product.getBuyerUser(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getDateClosed());
            completedAuctionRepository.save(completedAuction);
            logger.info("Inserted new completed auction to database");
            BankAccount seller = bankAccountRepository.findByUsername(product.getCreatedUser());
            if (product.getBuyerUser() != null) {

                BankAccount buyer = bankAccountRepository.findByUsername(product.getBuyerUser());
                seller.setBalance(seller.getBalance() + product.getPrice());
                buyer.setBalance(buyer.getBalance() - product.getPrice());
                bankAccountRepository.save(buyer);
                bankAccountRepository.save(seller);
                logger.info("Transferring money from " +product.getBuyerUser() +" to "+seller.getUsername());
            }
            productRepository.delete(product);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String addProduct(@RequestBody ProductRequest productRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = new Product(userDetails.getUsername(), productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), productRequest.getDateClosed());
        productService.addProduct(product);
        logger.info("Adding new product");

        return "Product added successfully ";
    }


    @PutMapping("/buyProduct")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> buyProductFromAuctions(@RequestBody BuyRequest buyRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductId(buyRequest.getId());
        BankAccount userBankAccount = bankAccountRepository.findByUsername(userDetails.getUsername());
        if (product.getCreatedUser().equals(userDetails.getUsername()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The product cannot be purchased by the user himself"));
        if (product.getPrice() > userBankAccount.getBalance() && product.getPrice() < buyRequest.getPrice())
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your balance in credit card or price proposed can not buy the product"));
        product.setBuyerUser(userDetails.getUsername());
        product.setPrice(buyRequest.getPrice());

        productRepository.save(product);
        logger.info("Reserved product for user " + product.getBuyerUser());
        return ResponseEntity.ok(new MessageResponse("Product reserved successfully "));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductId(id);
        if (product.getCreatedUser().equals(userDetails.getUsername()))
            productService.deleteProduct(id);
        else
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Product can be deleted from the created user"));
        logger.info("Product with id " + id + " deleted successfully");
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }


    @GetMapping("/listall")
    @PreAuthorize("hasRole('USER') or  hasRole('ADMIN')")
    public List<Product> getAllProducts() {
        logger.info("Listing all products");
        return productService.getAllProducts();

    }

}

