package com.mysite.weatherviewer.repository.base;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@RequiredArgsConstructor
public abstract class BaseRepository {
    protected final SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
