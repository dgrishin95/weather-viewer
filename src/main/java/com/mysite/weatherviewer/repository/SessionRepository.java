package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.Session;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository extends BaseRepository {

    public SessionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Session save(Session session) {
        getCurrentSession().persist(session);
        return session;
    }
}
