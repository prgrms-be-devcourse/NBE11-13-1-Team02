package com.example.ilovecoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/menus",
            "/orders",
            "/admin/menus",
            "/admin/orders",
            "/admin/menus/deleted"
    })

    public String forward() {
        return "forward:/index.html";
    }
}