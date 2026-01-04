package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Service
public interface UserService {
    void add(User user);

    User findUserByUsername(String username);

    List<User> listUsers();

    User findByID(Long id);

    void update(User user);

    void delete(Long id);


}
