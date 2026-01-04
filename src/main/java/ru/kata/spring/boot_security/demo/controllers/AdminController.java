package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String printUsers(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<User> users = userService.listUsers();
        List<Role> allRoles = roleService.findAll();

        User currentUser = userService.findUserByUsername(userDetails.getUsername());

        model.addAttribute("users", users);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("currentUser", currentUser);
        return "admin/index";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam(value = "roleIds", required = false) Long[] roleIds,
                             @ModelAttribute("user") User user) {
        Set<Role> selectedRoles;
        if (roleIds == null) {
            selectedRoles = new HashSet<>();
        } else {
            selectedRoles = roleService.findAll().stream()
                    .filter(role -> Arrays.asList(roleIds).contains(role.getId()))
                    .collect(Collectors.toSet());
        }
        user.setRoles(selectedRoles);
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") long id,
                             @RequestParam(value = "roleIds", required = false) Long[] roleIds,
                             @ModelAttribute("user") User user) {
        user.setId(id);
        Set<Role> selectedRoles;
        if (roleIds == null) {
            selectedRoles = new HashSet<>();
        } else {
            selectedRoles = roleService.findAll().stream()
                    .filter(role -> Arrays.asList(roleIds).contains(role.getId()))
                    .collect(Collectors.toSet());
        }
        user.setRoles(selectedRoles);
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}

