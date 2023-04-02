package com.webshop.controllers;

import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.services.AccountsService;
import com.webshop.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductsService productsService;
    private final AccountsService accountsService;

    @GetMapping("/add-product")
    public ModelAndView getAddProduct(Model model) {
        model.addAttribute("product", new ProductDTO());
        var mav = new ModelAndView();
        mav.setViewName("add-product");
        return mav;
    }

    @PostMapping("/add-product")
    public ModelAndView postAddProduct(
            @ModelAttribute ProductDTO productDTO,
            Model model,
            Principal principal
    ){
        Account seller = accountsService.findAccountByPrincipal(principal);
        var product = new Product(
                productDTO.getTitle(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                seller
        );
        seller.addProduct(product);
        productsService.addProduct(product);
        var mav = new ModelAndView();
        mav.setViewName("success");
        model.addAttribute("added", productDTO);
        return mav;
    }

    @GetMapping("/products/{id}")
    public ModelAndView productPage(
            @PathVariable(name = "id") Long id,
            Model model,
            Principal principal
    ) throws ProductNotFoundException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("product");
        model.addAttribute("account", accountsService.findAccountByPrincipal(principal));
        model.addAttribute("product", productsService.getProductById(id));
        return mav;
    }

    @GetMapping("/products")
    public ModelAndView getAllProducts(Model model) {
        var mav = new ModelAndView();
        mav.setViewName("products");
        model.addAttribute("products", productsService.getAllActiveProducts());
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchPage(
            @RequestParam(name = "search") String search,
            Model model
    ) {
        List<ProductDTO> result = productsService.findProductByName(search);
        var mav = new ModelAndView();
        model.addAttribute("products", result);
        mav.setViewName("search");
        return mav;
    }

    @GetMapping("/products/{id}/delete")
    public ModelAndView productDeleteGet(
            @PathVariable(name = "id") Long id,
            Model model,
            Principal principal
    ) throws ProductNotFoundException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("product");
        model.addAttribute("account", accountsService.findAccountByPrincipal(principal));
        model.addAttribute("product", productsService.findProductById(id));
        model.addAttribute("delete", "");
        return mav;
    }

    @PostMapping("/products/{id}/delete")
    public ModelAndView productDelete(
            @PathVariable(name = "id") Long id,
            Model model,
            Principal principal
    ) throws ProductNotFoundException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("success");
        var product = productsService.findProductById(id);
        accountsService.deleteProductFromAccount(
                product,
                accountsService.findAccountByPrincipal(principal)
        );
        productsService.deleteProduct(id);
        model.addAttribute("deleted", "");
        return mav;
    }

    @ExceptionHandler({
            ProductNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(
            Exception e
    ) {
        var mav = new ModelAndView();
        mav.setViewName("product");
        mav.addObject("exception", e);
        return mav;
    }
}
