package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql("/sql/post-service-test-data.sql")
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;
    @MockBean
    private UserService userService;

    @Test
    void getPostById는_존재하는_게시물을_내려준다() {
        // given
        // when
        PostEntity result = postService.getPostById(1);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
    }

    @Test
    void getPostById는_존재하지_않는_게시물은_에러를_던진다() {
        // given
        // when & then
        assertThatThrownBy(() -> postService.getPostById(99999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void postCreateDto를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .writerId(1)
                .content("foobar")
                .build();
        UserEntity writer = new UserEntity();
        writer.setId(1L);
        writer.setEmail("kok2@gmail.com");
        writer.setNickname("kok2");
        writer.setAddress("Seoul");
        writer.setStatus(UserStatus.ACTIVE);
        writer.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        BDDMockito.when(userService.getById(1)).thenReturn(writer);

        // when
        PostEntity result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
        assertThat(result.getWriter().getId()).isEqualTo(1);
    }


    @Test
    void postUpdateDto를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("hello world!!")
                .build();

        // when
        postService.update(1, postUpdateDto);

        // then
        PostEntity postEntity = postService.getPostById(1);
        assertThat(postEntity.getContent()).isEqualTo("hello world!!");
        assertThat(postEntity.getModifiedAt()).isNotNull();
    }

}