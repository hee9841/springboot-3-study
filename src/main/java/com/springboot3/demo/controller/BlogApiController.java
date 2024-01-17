package com.springboot3.demo.controller;

import com.springboot3.demo.domain.Article;
import com.springboot3.demo.dto.AddArticleRequest;
import com.springboot3.demo.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/blog")
public class BlogApiController {

    private final BlogService blogService;


    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.saveArticle(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedArticle);
    }

}
