package com.konkuk.sample.repository;

import com.konkuk.sample.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccountRepository {
    @PersistenceContext
    EntityManager em;

    public Account readOne(Long accountId){
        return em.find(Account.class, accountId);
    }

    public List<Account> readAllByMember(Long memberId) {
        return em.createQuery("select a from Account a inner join a.member m" +
                " where m.id = :memberId", Account.class)
                .setParameter("memberId", memberId)
                .getResultList(); // JPQL
    }

    public List<Long> readAllBalanceByMember(Long memberId) {
        return em.createQuery("select a.balance from Account a inner join a.member m" +
                " where m.id = :memberId", Long.class)
                .setParameter("memberId", memberId)
                .getResultList(); // JPQL
    }

    public Long create(Account account){
        em.persist(account);
        return account.getId();
    }

    public Long delete(Long accountId){
        Account account = em.find(Account.class, accountId);
        em.remove(account);
        return account.getId();
    }
}
