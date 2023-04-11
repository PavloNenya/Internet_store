package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.models.Cart;
import com.webshop.models.Product;
import com.webshop.repositories.CartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartsService {
    private final CartsRepository cartsRepository;

    public List<Cart> findOwnersCartList(Long id) {
        return cartsRepository.findCartsByOwnerId(id);
    }

    public Cart saveCart(Cart cart) {
        List<Cart> carts = findOwnersCartList(cart.getOwner().getId());
        if(!carts.contains(cart)) {
            return cartsRepository.save(cart);
        }
        return null;
    }

    public void deleteFromCart(
            Account account,
            Product product
    ) {
        cartsRepository.deleteCartByOwnerIdAndProductId(account.getId(), product.getId());
    }

    public boolean isProductInCartOfOwner(Product product, Account owner) {
        var res = cartsRepository.findByProductIdAndOwnerId(product.getId(), owner.getId());
        return res.isPresent();
    }

    public List<Product> getProductsFromCart(Account owner) {
        return findOwnersCartList(owner.getId()).stream()
                .map(Cart::getProduct)
                .toList();
    }

    public void emptyOwnersCart(Account owner) {
        cartsRepository.deleteAllByOwner(owner);
    }
}
