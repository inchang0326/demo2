package com.example.demo.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team team1 = new Team("A");
        Team team2 = new Team("B");

        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("inchang1");
        member1.changeTeam(team1);
        Member member2 = new Member("inchang2");
        member2.changeTeam(team1);
        Member member3 = new Member("inchang3");
        member3.changeTeam(team1);
        Member member4 = new Member("inchang4");
        member4.changeTeam(team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        members.forEach(m -> System.out.println(m.getTeam()));
    }
}