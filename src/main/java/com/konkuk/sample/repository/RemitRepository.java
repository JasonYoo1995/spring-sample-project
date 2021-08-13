package com.konkuk.sample.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RemitRepository {
    @PersistenceContext
    EntityManager em;

}
