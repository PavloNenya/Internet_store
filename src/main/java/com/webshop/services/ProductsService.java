package com.webshop.services;

import com.webshop.models.Product;
import com.webshop.repositories.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductService {
    ProductsRepository productsRepository;

    public ResponseEntity<List<Product>> getAllProducts() {
        // TODO: 19.03.2023
        return new ResponseEntity<>(
                StreamSupport.stream(productsRepository.findAll().spliterator(), false).toList(),
                HttpStatus.OK);
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }

    public void buyProduct(Product product, int accountId) {
        productsRepository.deleteById(product.getId());
    }

    public Optional<Product> getProductById(Long id) {
        return productsRepository.findById(id);
    }
}
