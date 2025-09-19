package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.Location;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository extends BaseRepository {

    public LocationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public boolean isLocationExist(String name, Long userId) {
        String existsQuery = "FROM Location s WHERE s.name = :name AND s.user.id = :userId";

        Long count = getCurrentSession()
                .createQuery(existsQuery, Long.class)
                .setParameter("name", name)
                .setParameter("userId", userId)
                .getSingleResult();

        return count > 0;
    }

    public Optional<Location> findByNameAndUserId(String name, Long userId) {
        return Optional.ofNullable(getCurrentSession()
                .createQuery("FROM Location s WHERE LOWER(s.name) = LOWER(:name) AND s.user.id = :userId",
                        Location.class)
                .setParameter("name", name)
                .setParameter("userId", userId)
                .uniqueResult());
    }

    public Location save(Location location) {
        getCurrentSession().persist(location);
        return location;
    }
}
