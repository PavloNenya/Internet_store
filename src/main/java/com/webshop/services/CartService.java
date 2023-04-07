package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.models.Cart;
import com.webshop.models.Product;
import com.webshop.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public List<Cart> findCartList(Long id) {
        return cartRepository.findCartsByOwnerId(id);
    }

    public void saveCart(Cart cart) {
        List<Cart> carts = findCartList(cart.getOwner().getId());
        if(!carts.contains(cart)) {
            cartRepository.save(cart);
        }
    }

    public void deleteFromCart(
            Account account,
            Product product
    ) {
        cartRepository.deleteCartByOwnerIdAndProductId(account.getId(), product.getId());
    }

    public boolean isProductInCartByOwner(Product product, Account owner) {
        var res = cartRepository.findByProductIdAndOwnerId(product.getId(), owner.getId());
        return res.isPresent();
    }

    public List<Product> getProductsFromCart(Account owner) {
        return findCartList(owner.getId()).stream()
                .map(Cart::getProduct)
                .toList();
    }

    public void emptyOwnersCart(Account owner) {
        cartRepository.deleteAllByOwner(owner);
    }
}
