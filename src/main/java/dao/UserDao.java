package dao;

import model.User;

import java.util.List;

public interface UserDao {
    void save(User user);

    void update(User user);

    void delete(Long id);

    List<User> listUsers();

    public User findByID(Long id);

}