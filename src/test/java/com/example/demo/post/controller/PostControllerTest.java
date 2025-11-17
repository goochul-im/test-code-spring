package com.example.demo.post.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회할_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(new TestClockHolder(307L), new TestUuidHolder("test-uuid"));
        User writer = testContainer.userRepository.save(User.builder()
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa").build());
        testContainer.postRepository.save(Post.builder()
                .writer(writer)
                .createdAt(300L)
                .content("hello world")
                .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

        //then
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(300L);
        assertThat(result.getBody().getModifiedAt()).isNull();
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("gooch123");
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_404_응답을_받는다() {
        //given
        TestContainer testContainer = new TestContainer(new TestClockHolder(307L), new TestUuidHolder("test-uuid"));
        User writer = testContainer.userRepository.save(User.builder()
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa").build());
        testContainer.postRepository.save(Post.builder()
                .writer(writer)
                .createdAt(300L)
                .content("hello world")
                .build());

        //when & then
        assertThatThrownBy(() -> testContainer.postController.getPostById(2L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();

        TestContainer testContainer = new TestContainer(new TestClockHolder(307L), new TestUuidHolder("test-uuid"));
        User writer = testContainer.userRepository.save(User.builder()
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa").build());
        testContainer.postRepository.save(Post.builder()
                .writer(writer)
                .createdAt(300L)
                .content("hello world")
                .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, postUpdate);

        //then
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("foobar");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(300L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("gooch123");

    }
}
