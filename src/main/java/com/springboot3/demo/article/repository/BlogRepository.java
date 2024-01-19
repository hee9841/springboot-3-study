package com.springboot3.demo.article.repository;


import com.springboot3.demo.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {

}
