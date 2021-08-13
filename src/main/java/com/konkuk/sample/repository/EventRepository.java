package com.konkuk.sample.repository;

import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class EventRepository {
    @PersistenceContext
    EntityManager em;

    public Event readOneByMember(Member member) {
        return em.createQuery("select e from Event e where e.member = :member", Event.class)
                .setParameter("member", member)
                .getSingleResult(); // JPQL
    }

    public Long create(Event event){
        em.persist(event);
        return event.getId();
    }

    public Long update(Event event){
        em.merge(event);
        return event.getId();
    }
}
