package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.WeatherData;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class WeatherDataRepository extends BaseRepository {

    public WeatherDataRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public WeatherData findByLocationId(Long locationId) {
        return getCurrentSession()
                .createQuery("FROM WeatherData WHERE location.id = :locationId", WeatherData.class)
                .setParameter("locationId", locationId)
                .uniqueResult();
    }

    public WeatherData save(WeatherData newWeatherData) {
        getCurrentSession().merge(newWeatherData);
        return newWeatherData;
    }

    public List<WeatherData> findByUserId(Long userId) {
        return getCurrentSession()
                .createQuery(
                        "FROM WeatherData wd JOIN FETCH wd.location WHERE wd.location.user.id = :userId",
                        WeatherData.class)
                .setParameter("userId", userId)
                .list();
    }
}
