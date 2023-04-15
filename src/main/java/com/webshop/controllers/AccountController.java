package com.webshop.controllers;

import com.webshop.models.Account;
import com.webshop.services.AccountsService;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountsService accountsService;
    private final OrdersService ordersService;
    private final ProductsService productsService;
    @GetMapping("/account")
    public ModelAndView account(
            Principal principal
    ) {
        var mav = new ModelAndView();
        System.out.println(principal.toString());
        Account account = accountsService.findAccountByPrincipal(principal);
        mav.addObject("account", account);
        mav.addObject("bought", ordersService.boughtProducts(account));
        mav.addObject("products", productsService.findProductsBySeller(account));
        mav.setViewName("account");
        return mav;
    }
}
