package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    
    @Test
    void PostCreate로_Post를_생성할_수_있다(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello world")
                .build();
        User writer = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();


        //when
        Post result = Post.from(writer, postCreate, new TestClockHolder(307L));

        //then
        assertThat(result.getContent()).isEqualTo("hello world");
        assertThat(result.getWriter().getEmail()).isEqualTo(writer.getEmail());
        assertThat(result.getWriter().getNickname()).isEqualTo(writer.getNickname());
        assertThat(result.getWriter().getAddress()).isEqualTo(writer.getAddress());
        assertThat(result.getWriter().getStatus()).isEqualTo(writer.getStatus());
        assertThat(result.getWriter().getCertificationCode()).isEqualTo("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa");
        assertThat(result.getCreatedAt()).isEqualTo(307L);
    }

    @Test
    void PostUpdate로_Post를_업데이트_할_수_있다(){
        //given
        User writer = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();
        Post post = Post.builder()
                .id(1L)
                .writer(writer)
                .createdAt(300L)
                .content("hello world")
                .build();

        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello spring")
                .build();

        //when
        Post result = post.update(postUpdate, new TestClockHolder(307L));

        //then
        assertThat(result.getContent()).isEqualTo("hello spring");
        assertThat(result.getModifiedAt()).isEqualTo(307L);
        assertThat(result.getWriter().getEmail()).isEqualTo(writer.getEmail());
        assertThat(result.getWriter().getNickname()).isEqualTo(writer.getNickname());
        assertThat(result.getWriter().getAddress()).isEqualTo(writer.getAddress());
        assertThat(result.getWriter().getStatus()).isEqualTo(writer.getStatus());
        assertThat(result.getWriter().getCertificationCode()).isEqualTo("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa");
    }

}
