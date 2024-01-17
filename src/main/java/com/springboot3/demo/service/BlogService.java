package com.springboot3.demo.service;

import com.springboot3.demo.domain.Article;
import com.springboot3.demo.dto.AddArticleRequest;
import com.springboot3.demo.repository.BlogRepository;
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

}
