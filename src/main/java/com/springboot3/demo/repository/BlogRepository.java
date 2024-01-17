package com.springboot3.demo.repository;


import com.springboot3.demo.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {

}
