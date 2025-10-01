package com.mysite.weatherviewer.repository;

import com.mysite.weatherviewer.model.WeatherData;
import com.mysite.weatherviewer.repository.base.BaseRepository;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserWeatherRepository extends BaseRepository {

    public UserWeatherRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
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
