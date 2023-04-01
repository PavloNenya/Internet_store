package com.webshop.services;

import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.repositories.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductsService {
    ProductsRepository productsRepository;

    public List<ProductDTO> getAllProducts() {
        return StreamSupport.stream(productsRepository.findAll().spliterator(), false)
                .map(this::productToDTO)
                .toList();
    }

    private ProductDTO productToDTO(Product product) {
        return new ProductDTO(
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getId()
        );
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }

    // TODO: 28.03.2023
    public Product buyProduct(Long productId) {
        var productToDelete = findProductById(productId);
        productToDelete.setActive(false);
        productsRepository.updateProductActivity(productId, false);
        return productToDelete;
    }

    public Product getProductById(Long id) {
        return productsRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<ProductDTO> findProductByName(String search) {
        return getAllProducts()
                .stream()
                .filter(p -> p.getTitle().contains(search))
                .toList();
    }

    public List<Product> findProductsBySeller(Account account) {
        return StreamSupport
                .stream(productsRepository.findProductsBySeller(account).spliterator(), false)
                .toList();
    }

    public Product findProductById(Long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<ProductDTO> getAllActiveProducts() {
        return StreamSupport.stream(productsRepository.findAll().spliterator(), false)
                .filter(Product::isActive)
                .map(this::productToDTO)
                .toList();
    }
}
