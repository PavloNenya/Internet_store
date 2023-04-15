package com.webshop.controllers;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.exceptions.IncorrectEmailException;
import com.webshop.services.AccountsService;
import com.webshop.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountsService accountsService;

    @GetMapping("/register")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("accountDTO", new AccountDTO());
        mav.setViewName("register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView create(
            @ModelAttribute(name = "accountDTO") AccountDTO accountDTO
    ) throws AccountAlreadyExistException, IncorrectEmailException {
        System.out.println();
        authenticationService.validation(accountDTO);
        accountsService.existsByEmail(accountDTO);
        ModelAndView mav = new ModelAndView();
        accountsService.save(accountDTO);
        mav.setViewName("success");
        mav.addObject("registered", "Account registered successfully");
        return mav;
    }
}
