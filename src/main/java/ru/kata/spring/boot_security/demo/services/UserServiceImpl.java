package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserWithRolesByUsername(username);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByID(Long id) {
        return userRepository.findByIdWIthRoles(id);
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserWithRolesByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }

        System.out.println("Найден пользователь: " + user.getUsername());
        System.out.println("Пароль в БД: " + user.getPassword());
        System.out.println("Роли: " + user.getRoles());

        return user;
    }
}

