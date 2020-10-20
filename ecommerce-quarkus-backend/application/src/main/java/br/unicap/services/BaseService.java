package br.unicap.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

public class BaseService<T> {

    @PersistenceContext
    private EntityManager em;

    private final Class<T> persistClass;

    public BaseService(Class<T> persistClass) {
        this.persistClass = persistClass;
    }

    @Transactional
    public T insert(T resource) {
        em.persist(resource);
        return resource;
    }

    @Transactional
    public T update(T resource) {
        em.merge(resource);
        return resource;
    }

    public List<T> findAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(persistClass);
        Root<T> rootEntry = cq.from(persistClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }
}

