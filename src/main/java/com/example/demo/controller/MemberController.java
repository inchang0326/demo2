package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

//    @PostConstruct
//    public void init() {
//        memberRepository.save(new Member("inchang"));
//        memberRepository.save(new Member("inchang2"));
//        memberRepository.save(new Member("inchang3"));
//        memberRepository.save(new Member("inchang4"));
//        memberRepository.save(new Member("inchang5"));
//        memberRepository.save(new Member("inchang6"));
//        memberRepository.save(new Member("inchang7"));
//        memberRepository.save(new Member("inchang8"));
//        memberRepository.save(new Member("inchang9"));
//        memberRepository.save(new Member("inchang10"));
//    }

    @GetMapping("/get/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.toString();
    }

    // Web 확장 - 도메인 클래스 컨버터 기능
    // 파라미터가 PK면서 위 처럼 컨버팅 코드가 필요할 때 JPA가 임의로 수행
    @GetMapping("/get/convert/{id}")
    public String findMemberDomainConverter(@PathVariable("id") Member member) {
        return member.toString();
    }

    @GetMapping("/get/members") // 웹 확장 - URL에 page=?&size=?&sort=? 쿼리 스트링 적용가능함 JPA가 알아서 바인딩 해준다.
    public Page<MemberDto> findMembers(@PageableDefault(size = 4) Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }
}
