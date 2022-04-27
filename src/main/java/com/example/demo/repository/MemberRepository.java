package com.example.demo.repository;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    /*
        쿼리 메소드
        (1) 메소드 이름으로 쿼리 생성
        (2) JPA NamedQuery : JPQL을 필요한 Entity 내 Global로 정의한 후 EntityManager를 통해 간단히 create하여 사용함
        (3) @Query

        + 복잡한 동적 쿼리는 QueryDSL
     */

    // 쿼리가 간단하고, 매개변수가 적으면 > (1)
    // 장점은 간단함 + entity 필드명이 바뀌면 컴파일 오류가 발생하여, 컴파일 시점에 오류가 있다는 것을 알려줌
    // Member findByName(String name);
    List<Member> findByAgeGreaterThan(int age);

    // 실무에 거의 사용되지 않음 > (2)
    // Entity에 쿼리를 작성하는 것과 달리, JPA가 repo 파일에 바로 쿼리를 작성할 수 있는 기능을 제공하기 때문에 비교적 잘 사용되지 않음
    // 장점은 JPQL의 문법 오류를 컴파´일 시점에 발견하게 해줌
    @Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);

    // 쿼리가 복잡하고, 매개변수가 많으면 > (3)
    // Entity에 쿼리를 작성하는 (2)와 달리, repo 파일에 바로 쿼리를 작성할 수 있음
    // JPQL의 문법 오류를 컴파일 시점에 발견하게 해줌
    @Query("select m from Member m where m.name = :name and m.age >= :age")
    Member findByNameAndAgeEquals(@Param("name") String name, @Param("age") int age);

    // 값 조회 가능
    @Query("select m.name from Member m")
    List<String> findNameList();

    // Dto 조회 가능
    @Query("select new com.example.demo.dto.MemberDto(m.id, m.name, t.name) from Member m join Team t")
    List<MemberDto> findMemberDtoList();

    @Query("select m from Member m where m.name in :names")
    List<Member> findMemberListByName(@Param("names") Collection<String> names);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /*
    *  @Modifying : 다중업데이트를 위한 executeUpdate 수행한다.
    *               해당 어노테이션이 없으면 getResult를 수행한다.
    *  *참고 : 일반적으로 JPA의 업데이트는 setter 메소드를 통해 더티체킹(변경감지)가 발생하여,
    *         트랜잭션 커밋 시점에 변경이 발생하면 update 쿼리가 발생하는데, 이는 단건 용 이다.    *
    *  *참고 : 벌크연산은 영속성 컨텍스트를 거치지 않고 곧장 DB에 반영된다.
    *         따라서 값의 정합성이 맞지 않을 수 있으므로, 벌크연산 후엔 바로 영속성 컨텍스트를 날려야 한다.
    *  >> @Modifying(clearAutomatically = true) 속성으로 해결 또는 벌크연산 이후 종료하도록 로직 수행
    * https://velog.io/@roro/JPA-JPQL-update-%EC%BF%BC%EB%A6%AC%EB%B2%8C%ED%81%AC%EC%99%80-%EC%98%81%EC%86%8D%EC%84%B1-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8
    * */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // @EntityGraph는 위 findMembersFetchJoin()의 JPQL을 대체하며, 쿼리메소드에서 유연하게 사용가능 함
    // + EntityGraph도 NamedQuery 처럼 사용할 수 있는 NamedEntityGraph를 제공함
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // SQL Hint X. 해당 힌트를 사용하면 JPA는 더티체킹(변경감지)을 하지 않음
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyMemberByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockMemberByName(String name);

    // List<NameOnly> findProjectionsByName(@Param("name") String name);
    <T> List<T> findProjectionsByName(@Param("name") String name, Class<T> type);

    @Query(value = "select * from member where member_id = ?", nativeQuery = true)
    Member findByNativeQuery(Long id);

    @Query(value = "select m.member_id as id, m.name as memberName, t.name as teamName from Member m inner join Team t on m.team_id = t.team_id"
            , countQuery = "select count(*) from member"
            , nativeQuery = true)
    Page<MemberProjections> findByNativeProjections(Pageable pageable);
}
