package br.unicap.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class BaseService<T> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public T insert(T resource) {
        em.persist(resource);
        return resource;
    }

}
