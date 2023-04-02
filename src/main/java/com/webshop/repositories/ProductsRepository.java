package com.webshop.repositories;

import com.webshop.models.Account;
import com.webshop.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    Iterable<Product> findProductsBySeller(Account account);
    @Modifying
    @Query("UPDATE products SET isActive = :activity WHERE id = :productId")
    @Transactional
    void updateProductActivity(@Param("productId") Long productId,
                               @Param("activity") boolean activity);
}
