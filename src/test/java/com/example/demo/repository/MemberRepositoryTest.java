package com.example.demo.repository;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testMember() {
        Member member = new Member("inchang");
        member.setAge(10);
        Member member2 = new Member("inchang2");
        member2.setAge(10);
        Member savedMember = memberRepository.save(member);
        memberRepository.save(member2);

        Optional<Member> opMember = memberRepository.findById(savedMember.getId());
        Member findMember = opMember.get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getName()).isEqualTo(savedMember.getName());
//        System.out.println(memberRepository.findByName("inchang").getId());
//        List<Member> memberList = memberRepository.findByAgeGreaterThan(5);
//        memberList.forEach(m -> System.out.println(m.getName()));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();
        long totalCnt = page.getTotalElements();
        long totalPage = page.getTotalPages();

        content.forEach(m -> System.out.println(m.getName()));
        System.out.println(totalCnt);
        System.out.println(totalPage);

        Page<MemberDto> map = page.map(m -> new MemberDto(member.getId(), member.getName(), null));
    }

    @Test
    public void findMemberLazy() {
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = new Member("member1", 20, team1);
        Member member2 = new Member("member1", 20, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        /*  fetch 타입이 LAZY이므로, member는 초기화하나 team은 프록시로 초기화함
            n+1 문제 발생: 1은 member list 조회 + 조회한 member 수 n 만큼 n번 쿼리
        */
        // List<Member> allMember = memberRepository.findAll();

        /*  fetch 타입이 LAZY이더라도, 한방쿼리로 member와 team을 함께 조회할 수 있게 해줌
            즉, 성능상 유리할 수 있도록(항상 전체 조회하진 않으므로) 기본 타입은 LAZY로 가져가되,
            필요한 경우에 따라서 LAZY 타입에서 fetch join을 사용해서 한방 쿼리 역할을 한다.

            Q. 그러면 애초에 EAGER로 한번에 조회하면 되지 않나?
            A. EAGER로 조회하더라도, 수행된 총 쿼리내역은 LAZY와 동일하게 n+1로 수행 됨.

            Q. 그냥 join이 포함된 JPQL을 사용하면 되지 않나?
            A. LAZY이므로, join 이후에 team을 조회할때 FTS로 한 번 더 조회하게 됨.
               즉 위 처럼 수행된 총 쿼리내역은 n+1로 수행 됨.

            Q. EAGER + join은?
            A. 위와 동일.
         */
        List<Member> allMember = memberRepository.findAll();

        allMember.forEach(m -> System.out.println(m.getTeam().getName()));
    }
}