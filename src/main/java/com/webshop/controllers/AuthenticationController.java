package com.webshop.controllers;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.exceptions.IncorrectEmailException;
import com.webshop.services.AccountsService;
import com.webshop.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountsService accountsService;

    @GetMapping("/register")
    public ModelAndView create(Model model){
        model.addAttribute("accountDTO", new AccountDTO());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView create(
            @ModelAttribute AccountDTO accountDTO
    ) throws AccountAlreadyExistException, IncorrectEmailException {
        authenticationService.validation(accountDTO);
        accountsService.existsByEmail(accountDTO.getEmail());
        ModelAndView mav = new ModelAndView();
        accountsService.save(accountDTO);
        mav.setViewName("success");
        mav.addObject("registered", "Account registered successfully");
        return mav;
    }

    @ExceptionHandler({
            AccountAlreadyExistException.class,
            IncorrectEmailException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(Exception e, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        model.addAttribute("exception", e);
        model.addAttribute("accountDTO", new AccountDTO());
        return mav;
    }
}
