package com.auction.springvproject.security.services;

import com.auction.springvproject.models.Product;
import com.auction.springvproject.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        repository.save(product);
    }

    public Product getProductId(Long id) {
       return productRepository.getReferenceById(id);
    }

    public void deleteProduct(Long id) {
    productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.listProductByTime();
    }
}
