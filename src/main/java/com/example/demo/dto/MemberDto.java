package com.example.demo.dto;

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
}
