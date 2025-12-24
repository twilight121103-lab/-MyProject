package org.example.springcrud.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.springcrud.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByID(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.createQuery("UPDATE User u SET u.firstName = :name, u.lastName = :lastName, u.email = :email WHERE u.id = :id")
                .setParameter("name", user.getFirstName())
                .setParameter("lastName", user.getLastName())
                .setParameter("email", user.getEmail())
                .setParameter("id", user.getId())
                .executeUpdate();
    }

    @Override
    public void delete(Long id) {
        User user = findByID(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public List<User> listUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();

    }
}