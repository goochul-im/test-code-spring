package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostServiceImplTest {

    private PostServiceImpl postService;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository userRepository = new FakeUserRepository();

        this.postService = PostServiceImpl.builder()
                .clockHolder(new TestClockHolder(307L))
                .userRepository(userRepository)
                .postRepository(fakePostRepository)
                .build();

        User writer = userRepository.save(User.builder()
                .id(1L)
                .email("kok2@gmail.com")
                .nickname("kok2")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(0L)
                .build());

        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(307L)
                .writer(writer)
                .build());
    }

    @Test
    void getPostById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getPostById(1);

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

        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isEqualTo(307L);
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
        Post post = postService.getPostById(1);
        assertThat(post.getContent()).isEqualTo("hello world!!");
        assertThat(post.getModifiedAt()).isEqualTo(307L);
    }

}
