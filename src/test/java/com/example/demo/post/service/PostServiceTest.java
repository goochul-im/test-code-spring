package com.example.demo.post.service;

import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.post.service.PostService;
import com.example.demo.user.service.UserService;
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
        PostCreate postCreate = PostCreate.builder()
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
        PostEntity result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
        assertThat(result.getWriter().getId()).isEqualTo(1);
    }


    @Test
    void postUpdateDto를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world!!")
                .build();

        // when
        postService.update(1, postUpdate);

        // then
        PostEntity postEntity = postService.getPostById(1);
        assertThat(postEntity.getContent()).isEqualTo("hello world!!");
        assertThat(postEntity.getModifiedAt()).isNotNull();
    }

}
