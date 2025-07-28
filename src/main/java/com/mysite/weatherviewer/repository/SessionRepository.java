package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.Session;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import java.time.Instant;
import java.util.UUID;
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

    public void remove(UUID uuid) {
        org.hibernate.Session session = getCurrentSession();
        Session foundSession = session.get(Session.class, uuid);

        if (foundSession != null) {
            session.remove(foundSession);
        }
    }

    public Session findByUuid(UUID id) {
        return getCurrentSession()
                .createQuery("FROM Session WHERE id = :id", Session.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public boolean isSessionActive(UUID id) {
        String existsQuery = "SELECT count(s) FROM Session s WHERE s.id = :id AND s.expiresAt > :now";

        Long count = getCurrentSession()
                .createQuery(existsQuery, Long.class)
                .setParameter("id", id)
                .setParameter("now", Instant.now())
                .getSingleResult();

        return count > 0;
    }
}
