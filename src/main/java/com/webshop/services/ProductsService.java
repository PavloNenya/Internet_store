package com.webshop.services;

import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.repositories.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductsService {
    ProductsRepository productsRepository;

    public List<ProductDTO> getAllProducts() {
        return productsRepository.findAll().stream()
                .map(this::productToDTO)
                .toList();
    }

    private ProductDTO productToDTO(Product product) {
        return new ProductDTO(
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getSeller().getId(),
                product.getId()
        );
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }

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
        return productsRepository.findAll().stream()
                .filter(Product::isActive)
                .map(this::productToDTO)
                .toList();
    }

    public void deleteProduct(Long id) {
        productsRepository.updateProductActivity(id, false);
    }
}
