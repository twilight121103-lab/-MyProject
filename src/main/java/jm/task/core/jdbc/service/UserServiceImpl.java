package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDaoJDBCImpl userDaoImpl = new UserDaoJDBCImpl();
    @Override
    public void createUsersTable() {
        userDaoImpl.createUsersTable();
    }
    @Override
    public void dropUsersTable() {
        userDaoImpl.dropUsersTable();
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDaoImpl.saveUser(name, lastName, age);
    }
    @Override
    public void removeUserById(long id) {
        userDaoImpl.removeUserById(id);
    }
    @Override
    public List<User> getAllUsers() {
        return(userDaoImpl.getAllUsers());
    }
    @Override
    public void cleanUsersTable() {
        userDaoImpl.cleanUsersTable();
    }
}
