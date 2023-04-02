package com.webshop.controllers;

import com.webshop.exceptions.BuyingOwnProductException;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.services.AccountsService;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    ) throws ProductNotFoundException {
        var mav = new ModelAndView();
        mav.setViewName("buy-product");
        var logged = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        if(logged.getId().equals(product.getSeller().getId())) {
            throw new BuyingOwnProductException();
        }
        mav.addObject("account", logged);
        mav.addObject("product", product);
        return mav;
    }
    @PostMapping("/buy-product/{id}")
    public ModelAndView newOrderPost(
            @PathVariable Long id,
            Principal principal
    ) throws ProductNotFoundException {
        var mav = new ModelAndView();
        var buyer = accountsService.findAccountByPrincipal(principal);
        var product = productsService.getProductById(id);
        ordersService.formOrder(buyer, product.getSeller(), product);
        productsService.buyProduct(product.getId());
        mav.setViewName("success");
        mav.addObject("bought");
        return mav;
    }

    @ExceptionHandler({
            ProductNotFoundException.class,
            BuyingOwnProductException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(
            Exception e
    ) {
        var mav = new ModelAndView();
        mav.setViewName("buy-product");
        mav.addObject("exception", e);
        return mav;
    }
}
