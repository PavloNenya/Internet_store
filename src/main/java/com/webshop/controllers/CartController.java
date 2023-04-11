package com.webshop.controllers;

import com.webshop.models.Cart;
import com.webshop.models.Product;
import com.webshop.services.AccountsService;
import com.webshop.services.CartsService;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final AccountsService accountsService;
    private final CartsService cartsService;
    private final ProductsService productsService;
    private final OrdersService ordersService;

    @GetMapping("/cart")
    public ModelAndView cart(Principal principal) {
        ModelAndView mav = new ModelAndView();
        var account = accountsService.findAccountByPrincipal(principal);
        var cart = cartsService.findOwnersCartList(account.getId());
        List<Product> cartProducts = productsService.getProductsFromCart(cart);
        mav.addObject("cart", cartProducts);

        System.out.println(cartProducts);

        mav.setViewName("cart");
        return mav;
    }

    @PostMapping("/cart/add/{id}")
    public ModelAndView addToCartPost(
            @PathVariable Long id,
            Principal principal,
            ModelAndView mav,
            HttpServletResponse response
    ) throws IOException {
        var account = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        var cart = new Cart(
                product,
                account
        );
        cartsService.saveCart(cart);
        mav.addObject("addedToCart");
        mav.setViewName("product");
        response.sendRedirect("/products/" + id);
        return mav;
    }

    @GetMapping("/cart/add/{id}")
    public ModelAndView addToCartGet(
            @PathVariable Long id,
            ModelAndView mav,
            HttpServletResponse response
    ) throws IOException {
        mav.setViewName("product");
        response.sendRedirect("/products/" + id);
        return mav;
    }

    @PostMapping("/cart/delete/{id}")
    public ModelAndView deleteProductFromCart(
            @PathVariable Long id,
            Principal principal,
            HttpServletResponse response
    ) throws IOException {
        ModelAndView mav = new ModelAndView();
        var account = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        cartsService.deleteFromCart(account, product);
        mav.setViewName("cart");
        response.sendRedirect("/cart");
        return mav;
    }

    @GetMapping("/cart/delete/**")
    public void deleteProductFromCartGet(HttpServletResponse response) throws IOException {
        response.sendRedirect("/cart");
    }

    @PostMapping("/cart/buy")
    public ModelAndView buyAllPost(
            Principal principal,
            HttpServletResponse response
    ) throws IOException {
        ModelAndView mav = new ModelAndView();
        var buyer = accountsService.findAccountByPrincipal(principal);
        List<Product> products = cartsService.getProductsFromCart(buyer);
        ordersService.buyCart(products, buyer);
        productsService.buyProducts(products);
        cartsService.emptyOwnersCart(buyer);
        mav.setViewName("success");
        mav.addObject("bought", "");
        response.sendRedirect("/cart/buy");
        return mav;
    }

    @GetMapping("/cart/buy")
    public ModelAndView buyAllGet() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("success");
        mav.addObject("bought", "");
        return mav;
    }
}

