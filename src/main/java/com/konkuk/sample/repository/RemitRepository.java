package com.konkuk.sample.repository;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Remit;
import com.konkuk.sample.domain.RemitType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RemitRepository {
    @PersistenceContext
    EntityManager em;

    public Remit readOne(Long id){
        return em.find(Remit.class, id);
    }

    public List<Remit> readByRemitType(Account account, RemitType remitType) {
        return em.createQuery("select r from Remit r where r.account = :account" +
                        " and r.type = :remitType", Remit.class)
                .setParameter("account", account)
                .setParameter("remitType", remitType)
                .getResultList(); // JPQL
    }

    public Long create(Remit remit){
        em.persist(remit);
        return remit.getId();
    }
}
