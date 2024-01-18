package com.springboot3.demo.service;

import com.springboot3.demo.domain.Article;
import com.springboot3.demo.dto.AddArticleRequest;
import com.springboot3.demo.dto.UpdateArticleRequest;
import com.springboot3.demo.repository.BlogRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor : final 이나 @NotNull이 붙은 필드로 생성자 만들어줌
@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article saveArticle(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }


    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

}
