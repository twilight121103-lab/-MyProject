package ru.kata.spring.boot_security.demo.controllers;

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
    public String printUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/show")
    public String showUserByParam(@RequestParam("id") long id, Model model) {
        User user = userService.findByID(id);
        model.addAttribute("user", user);
        return "admin/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        List<Role> allRoles = roleService.findAll();
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("user", user);

        return "admin/new";
    }

    @PostMapping()
    public String create(@RequestParam(value = "roleIds", required = false) Long[] roleIds, @ModelAttribute("user") User user) {
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

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") long id, Model model) {
        List<Role> allRoles = roleService.findAll();
        User user = userService.findByID(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String update(@RequestParam("id") long id, @RequestParam(value = "roleIds", required = false) Long[] roleIds, @ModelAttribute("user") User user) {
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
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}

