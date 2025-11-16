package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post를_통해_응답을_생성할_수_있다(){
        //given
        Post post = Post.builder()
                .content("hello world")
                .writer(User.builder()
                        .email("gooch123@naver.com")
                        .nickname("gooch123")
                        .address("Daegu")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                        .build())
                .build();

        //when
        PostResponse result = PostResponse.from(post);

        //then
        assertThat(result.getContent()).isEqualTo("hello world");
        assertThat(result.getWriter().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getWriter().getNickname()).isEqualTo("gooch123");
        assertThat(result.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
