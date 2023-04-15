package com.webshop.controllers.advice;

import com.webshop.dto.AccountDTO;
import com.webshop.dto.ProductDTO;
import com.webshop.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({
            AccountAlreadyExistException.class,
            IncorrectEmailException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView accountException(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        mav.addObject("exception", e);
        mav.addObject("accountDTO", new AccountDTO());
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

    @ExceptionHandler({
            IncorrectProductDataException.class
    })
    public ModelAndView sellWrongProductException(Exception e) {
        var mav = new ModelAndView();
        mav.setViewName("add-product");
        mav.addObject("product", new ProductDTO());
        mav.addObject("exception", e);
        return mav;
    }
}
