package com.springboot3.demo.dto;

import com.springboot3.demo.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO : 계층끼리 데이터를 교환하기 위한 객체 (단순 전달자 역할, 비지니스 로직을 포함 X)
 * DAO : 데이터베이스와 연결되고 데이터를 조회하고 수정하느데 사용
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
            .title(title)
            .content(content)
            .build();
    }
}
