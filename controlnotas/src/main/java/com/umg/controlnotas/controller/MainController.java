package com.umg.controlnotas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String Login(){ return "login";}

}
