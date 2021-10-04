package ru.mire.entity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {



    @GetMapping(value = {"/","/authorization"})
    public String authorization(){
        return "authorization";
    }

    @GetMapping(value = "/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping(value = "/registration_new_user")
    public String registrationNewUser(){
        return "redirect:/authorization";
    }
}
