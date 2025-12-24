package org.example.springcrud.controller;


import org.example.springcrud.model.User;
import org.example.springcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String printUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users/list"; // Будет искать в /WEB-INF/pages/users/list.html
    }

    @GetMapping("/show")
    public String showUserByParam(@RequestParam("id") long id, Model model) {
        User user = userService.findByID(id);
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") long id, Model model) {
        User user = userService.findByID(id);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/edit")
    public String update(@RequestParam("id") long id, @ModelAttribute("user") User user) {
        user.setId(id);
        userService.update(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}