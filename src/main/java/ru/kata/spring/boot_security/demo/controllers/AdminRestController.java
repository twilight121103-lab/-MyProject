package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public AdminRestController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.findUserByUsername(userDetails.getUsername()));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") Long id) {
        User user = userService.findByID(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Возвращаем ошибки валидации
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = new User();
            user.setUsername(userDTO.getEmail()); // email становится username
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setPassword(userDTO.getPassword());

            // Преобразуем roleIds в Set<Role>
            Set<Role> selectedRoles = new HashSet<>();
            if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
                List<Role> allRoles = roleService.findAll();
                selectedRoles = allRoles.stream()
                        .filter(role -> Arrays.asList(userDTO.getRoleIds()).contains(role.getId()))
                        .collect(Collectors.toSet());
            }
            user.setRoles(selectedRoles);

            userService.add(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                        @Valid @RequestBody UserDTO userDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        User userToBeUpdated = userService.findByID(id);
        if (userToBeUpdated == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Обновляем только необходимые поля
            userToBeUpdated.setUsername(userDTO.getEmail());
            userToBeUpdated.setFirstName(userDTO.getFirstName());
            userToBeUpdated.setLastName(userDTO.getLastName());
            userToBeUpdated.setAge(userDTO.getAge());

            // Пароль обновляем только если он предоставлен
            if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
                userToBeUpdated.setPassword(userDTO.getPassword());
            }

            // Обновляем роли
            Set<Role> selectedRoles = new HashSet<>();
            if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
                List<Role> allRoles = roleService.findAll();
                selectedRoles = allRoles.stream()
                        .filter(role -> Arrays.asList(userDTO.getRoleIds()).contains(role.getId()))
                        .collect(Collectors.toSet());
            }
            userToBeUpdated.setRoles(selectedRoles);

            userService.update(userToBeUpdated);
            return ResponseEntity.ok(userToBeUpdated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        User user = userService.findByID(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            userService.delete(id);
            return ResponseEntity.ok().body(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }
}