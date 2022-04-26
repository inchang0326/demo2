package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

//    @PersistenceContext
//    EntityManager entityManager;

    @Test
    public void saveOrMergeTest() {
        Item item = new Item("A");
        itemRepository.save(item); // save 시작전 - save 간 데이터 영속화 종료
        Optional<Item> findItem = itemRepository.findById(item.getId()); // 따라서 다시 조회 쿼리 발생
        // >> 영속화는 Transaction 생성 시작부터 종료까지의 라이프 사이클을 가지므로, 해당 클래스에 @Transactional 어노테이션을 적용해야 함
        System.out.println(findItem.get().getId());
    }
}