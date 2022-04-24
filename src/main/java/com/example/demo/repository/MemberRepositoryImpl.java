package com.example.demo.repository;

import com.example.demo.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom { // 실무에서 복잡한 동적 쿼리를 생성해야할 때 사용됨 + QueryDSL

    private final EntityManager em;

    @Override
    public List<Member> findMembers() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
