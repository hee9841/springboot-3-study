package com.springboot3.demo.test;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityMangerTest {
    @Autowired
    EntityManager em;


    public void example() {
        //엔티티 매니저가엔티티를 관리하지 않는 상태
        Member member = new Member(1L, "홍길동");

        em.persist(member); //엔티티가 관리 상태
        em.detach(member);  //엔티티 객체가 분리된 상태
        em.remove(member);  //엔티티 객체가 삭제된 상태
    }
}
