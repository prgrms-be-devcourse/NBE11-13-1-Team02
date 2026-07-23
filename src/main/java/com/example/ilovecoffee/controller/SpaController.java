package com.example.ilovecoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/menus",
            "/menus/{id}/detail",
            "/orders",
            "/admin/login",
            "/admin/menus",
            "/admin/menus/recents",
            "/admin/menus/deleted",
            "/admin/orders",
            "/admin/orders/{id}",
            "/admin/reviews",
            "/admin/menus/{id}/update",
            "/admin/menus/sold-out",
            "/admin/statistics"
    })
    public String forward() {
        return "forward:/index.html";
    }
}