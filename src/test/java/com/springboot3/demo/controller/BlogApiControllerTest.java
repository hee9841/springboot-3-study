package com.springboot3.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot3.demo.domain.Article;
import com.springboot3.demo.dto.AddArticleRequest;
import com.springboot3.demo.dto.UpdateArticleRequest;
import com.springboot3.demo.repository.BlogRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;    //직렬화, 역직렬화를 위한 클래스
    // 자바 객체를 JSON 데이터로 변환하는 직렬화,
    // JSON을 자바 객체로 변홯나는 역직렬화할때 사용

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();

        blogRepository.deleteAll();
    }


    @DisplayName("addArticle : 블로글 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        //given : 블로그 글 추가에 피요한 요청 객체 생성
        final String url = "/api/blog/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        //객체를 json으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        //when
        // 블로글 글 추가 API에 요청을 보냄. 이때 요청타입은 JSON, given절에 미리 만들어둔 객체를 요청 본문으로 함께 보냄
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

        //then :
        // 응답코드가 201 created인지 확인. Blog 전체를 조회해 크기가 1인지 확인하고 실제 저장된 데이터와 요청값을 비교
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }


    @DisplayName("findAllArticles : 블로그 글 목골 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        //given
        final String url = "/api/blog/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].title").value(title));

    }


    @DisplayName("findArticle: 블로글 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url = "/api/blog/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .build());

        //when
        ResultActions resultAction = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        resultAction
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("delteArticle: 블로글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url = "/api/blog/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .build());

        //when
        mockMvc.perform(delete(url, savedArticle.getId()))
            .andExpect(status().isOk());

        //then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        //given
        final String url = "/api/blog/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .build());

        final String newTitle = "title Update";
        final String newContent = "content Update";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
