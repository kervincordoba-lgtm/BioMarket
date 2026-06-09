package com.biomarket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping({"/404", "/404.html"})
    public String error404() {
        return "error/404";
    }
    @GetMapping({"/contact", "/contact.html"})
    public String contact() {
        return  "contact";
    }
    

}
