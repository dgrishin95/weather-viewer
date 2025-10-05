package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

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

    public User findById(Long id) {
        return getCurrentSession()
                .createQuery("FROM User WHERE id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
