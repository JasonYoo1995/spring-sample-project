package com.konkuk.sample.repository;

import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.MemberToMember;
import com.konkuk.sample.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    EntityManager em;

    public Member readOne(Long id){
        return em.find(Member.class, id);
    }
    public Member readOneByName(String name){
        try{
            return em.createQuery("select m from Member m" +
                        " where m.name = :name", Member.class)
                .setParameter("name", name)
                .getSingleResult();
        }
        catch (Exception e){
            throw new MemberNotFoundException("해당되는 회원이 존재하지 않습니다.");
        }
    }

    public List<Member> readAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList(); // JPQL
    }

    public Long create(Member member){
        em.persist(member);
        return member.getId();
    }

    public Long update(Member member){
        em.merge(member);
        return member.getId();
    }

    public Long createMemberToMember(MemberToMember memberToMember){
        em.persist(memberToMember);
        return memberToMember.getId();
    }

    public List<Member> readRegisteredMembers(Member registeringMember){
        return em.createQuery("select m.registeredMember from MemberToMember m" +
                " where m.registeringMember = :registeringMember", Member.class)
                .setParameter("registeringMember", registeringMember)
                .getResultList(); // JPQL
    }
}
