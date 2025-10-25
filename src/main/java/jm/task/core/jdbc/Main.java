package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        try {
            userService.createUsersTable();
            userService.saveUser("Иван", "Иванов", (byte) 20);
            userService.saveUser("Михаил", "Михайлов", (byte) 30);
            userService.saveUser("Никита", "Никитин", (byte) 20);
            userService.saveUser("Алексей", "Алексеев", (byte) 30);

            List<User> users = userService.getAllUsers();
            for (User user : users) {
                System.out.println(user);
            }
            userService.cleanUsersTable();
            userService.dropUsersTable();
        } finally {
            Util.closeConnection();
        }
    }
}
