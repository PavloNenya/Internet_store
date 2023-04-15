package com.webshop.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ModelAndView handleError() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        return mav;
    }
}
