package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;

    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userService.findUserByUsername(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        return "admin/index";
    }
}

