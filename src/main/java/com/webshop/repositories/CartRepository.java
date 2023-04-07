package com.webshop.repositories;

import com.webshop.models.Account;
import com.webshop.models.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findCartsByOwnerId(Long ownerId);
    @Modifying
    @Transactional
    void deleteCartByOwnerIdAndProductId(Long owner_id, Long product_id);
    Optional<Cart> findByProductIdAndOwnerId(Long productId, Long ownerId);

    @Transactional
    void deleteAllByOwner(Account owner);
}
