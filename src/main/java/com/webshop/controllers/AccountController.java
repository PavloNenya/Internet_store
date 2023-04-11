package com.webshop.controllers;

import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.services.AccountsService;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountsService accountsService;
    private final OrdersService ordersService;
    private final ProductsService productsService;
    @GetMapping("/account")
    public ModelAndView account(
            Model model,
            Principal principal
    ) {
        System.out.println("account get called");
        var mav = new ModelAndView();
        Account account = accountsService.findAccountByPrincipal(principal);
        List<Product> boughtProducts = ordersService.boughtProducts(account);
        model.addAttribute("account", account);
        model.addAttribute("bought", boughtProducts);
        model.addAttribute(
                "products",
                productsService.findProductsBySeller(account)
//                account.getProducts().stream()
//                        .filter(Product::isActive)
//                        .toList()
        );
        mav.setViewName("account");
        return mav;
    }
}
