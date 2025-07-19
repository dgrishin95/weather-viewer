package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public User save(User user) {
        Session session = getCurrentSession();
        session.persist(user);
        session.flush();

        return user;
    }
}
