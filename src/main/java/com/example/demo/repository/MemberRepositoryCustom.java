package com.example.demo.repository;

import com.example.demo.entity.Member;

import java.util.List;

// 실무에서 복잡한 동적 쿼리를 생성해야할 때 사용됨 + QueryDSL
public interface MemberRepositoryCustom {
    List<Member> findMembers();
}
