package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByID(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void update(User user) {
        User userToBeUpdated = userRepository.findUserById(user.getId());
        userToBeUpdated.setFirstName(user.getFirstName());
        userToBeUpdated.setLastName(user.getLastName());
        userToBeUpdated.setAge(user.getAge());
        String password = user.getPassword();
        userToBeUpdated.setPassword(passwordEncoder.encode(password));
        userToBeUpdated.setUsername(user.getUsername());
        userToBeUpdated.setRoles(user.getRoles());
        userRepository.save(userToBeUpdated);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
