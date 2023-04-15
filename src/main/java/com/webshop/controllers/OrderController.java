package com.webshop.controllers;

import com.webshop.exceptions.BuyingOwnProductException;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.services.AccountsService;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final AccountsService accountsService;
    private final OrdersService ordersService;
    private final ProductsService productsService;

    @GetMapping("/buy-product/{id}")
    public ModelAndView newOrderGet(
            @PathVariable Long id,
            Principal principal
    ) {
        var mav = new ModelAndView();
        mav.setViewName("buy-product");
        var logged = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        if(logged.getId().equals(product.getSeller().getId())) {
            throw new BuyingOwnProductException();
        }
        if(!product.isActive()) {
            throw new ProductNotFoundException();
        }
        mav.addObject("account", logged);
        mav.addObject("product", product);
        return mav;
    }
    @PostMapping("/buy-product/{id}")
    public ModelAndView newOrderPost(
            @PathVariable Long id,
            Principal principal
    ) {
        var mav = new ModelAndView();
        var buyer = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        ordersService.formOrder(buyer, product);
        productsService.buyProduct(product.getId());
        mav.setViewName("success");
        mav.addObject("bought", "");
        return mav;
    }


}
