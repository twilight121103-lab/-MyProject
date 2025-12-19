package service;

import model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    void add(User user);

    List<User> listUsers();

    @Transactional
    User findByID(Long id);

    void update(User user);

    void delete(Long id);
}