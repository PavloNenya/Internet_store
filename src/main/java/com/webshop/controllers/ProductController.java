package com.webshop.controllers;

import com.webshop.dto.ErrorDTO;
import com.webshop.dto.OrderDTO;
import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Order;
import com.webshop.models.Product;
import com.webshop.models.requests.OrderRequest;
import com.webshop.services.OrdersService;
import com.webshop.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
public class ProductRestController {
    private final ProductsService productsService;
    private final OrdersService ordersService;

    @PostMapping("/new-order")
    public ResponseEntity<Order> newOrder(@RequestBody OrderRequest orderRequest) {
//        Long productId = orderRequest.getProductId();
//        Long clientId = orderRequest.getClientId();
        return ResponseEntity.ok(ordersService.formOrder(new OrderDTO())); //stub
    }

    @GetMapping("/add-product")
    public ModelAndView getAddProduct(Model model) {
        model.addAttribute("product", new ProductDTO());
        var mav = new ModelAndView();
        mav.setViewName("add-product");
        System.out.println("get called");
        return mav;
    }

    // TODO: 26.03.2023
    @PostMapping("/add-product")
    public ModelAndView postAddProduct(
            @ModelAttribute ProductDTO productDTO,
            Model model
    ){
        Long id = 1L;//productRequest.getSellerId();
        model.addAttribute("product", productDTO);
        productsService.addProduct(new Product(
                productDTO.getTitle(), productDTO.getDescription(), productDTO.getPrice(), id
        ));
        var mav = new ModelAndView();
        mav.setViewName("success");
        return mav;
    }

    @GetMapping("/product/{id}")
    public ModelAndView productPage(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        Optional<Product> product = productsService.getProductById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("product");
            model.addAttribute(product.get());
            return mav;
        }
    }

    @GetMapping("/products")
    public ModelAndView getAllProducts(Model model) {
        var mav = new ModelAndView();
        mav.setViewName("products");
        List<ProductDTO> products = productsService.getAllProducts();
        model.addAttribute("products", products);
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchPage(
            @RequestParam(name = "search") String search,
            Model model
    ) {
        Iterable<ProductDTO> products = productsService.getAllProducts();
        List<ProductDTO> result = StreamSupport
                .stream(products.spliterator(), false)
                .filter(p -> p.getTitle().contains(search))
                .toList();
        var mav = new ModelAndView();
        model.addAttribute("products", result);
        mav.setViewName("search");
        return mav;
    }

    @ExceptionHandler({
            ProductNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorDTO> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
    }
}
