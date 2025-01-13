package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.lab.model.User;

@Stateless
public class UserRepository extends GenericRepository<User, Integer> {

    public UserRepository() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT * FROM find_user_by_username(:username)", User.class);
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
