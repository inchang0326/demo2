package com.example.demo.dto;

import com.example.demo.entity.Member;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;

    public MemberDto() {}

    public MemberDto(Long id, String username, String teamname) {
        this.id = id;
        this.username = username;
        this.teamname = teamname;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getName();
        this.teamname = null;
    }
}
