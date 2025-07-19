package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    public User findByLogin(String login) {
        return getCurrentSession()
                .createQuery("FROM User WHERE login = :login", User.class)
                .setParameter("login", login)
                .uniqueResult();
    }

    public boolean existsByLogin(String login) {
        String existsQuery = "SELECT count(u) FROM User u WHERE u.login = :login";
        Long count = getCurrentSession()
                .createQuery(existsQuery, Long.class)
                .setParameter("login", login)
                .getSingleResult();

        return count > 0;
    }

    public User save(User user) {
        getCurrentSession().persist(user);
        return user;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
