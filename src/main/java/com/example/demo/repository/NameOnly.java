package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Value;

public interface NameOnly {

//    @Value("#{target.name + ' ' + target.age}") // open Projections : select list 모두 가져와서 처리함.
    String getName();   // root Entity
    TeamInfo getTeam(); // 한계 : 중첩구조일 경우, Team의 select list는 최적화가 안되고 조인도 outer 조인만 수행됨.

    interface TeamInfo {
        String getName();
    }
}
