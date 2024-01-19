package com.springboot3.demo.article.controller;

import com.springboot3.demo.article.domain.Article;
import com.springboot3.demo.article.dto.AddArticleRequest;
import com.springboot3.demo.article.dto.ArticleResponse;
import com.springboot3.demo.article.dto.UpdateArticleRequest;
import com.springboot3.demo.article.service.BlogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/blog/articles")
public class BlogApiController {

    private final BlogService blogService;


    @GetMapping({"/", ""})
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
            .stream()
            .map(ArticleResponse::new)
            .toList();

        return ResponseEntity
            .ok()
            .body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
            .body(new ArticleResponse(article));
    }


    @PostMapping({"/", ""})
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.saveArticle(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
            .build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(updatedArticle);
    }
}
