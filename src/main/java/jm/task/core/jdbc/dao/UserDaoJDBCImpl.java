package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    // SQL запросы
    private final String SQL_CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(50) NOT NULL,
            last_name VARCHAR(50) NOT NULL,
            age TINYINT NOT NULL
        )
        """;

    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";
    private final String SQL_INSERT = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
    private final String SQL_DELETE_BY_ID = "DELETE FROM users WHERE id = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM users";
    private final String SQL_CLEAN_TABLE = "DELETE FROM users";

    private Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection();
    }

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
            System.out.println("Таблица 'users' создана или уже существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL_DROP_TABLE);
            System.out.println("Таблица 'users' удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("User с ID " + id + " удален");
            } else {
                System.out.println("User с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL_CLEAN_TABLE);
            System.out.println("Таблица 'users' очищена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
