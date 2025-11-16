package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void User로_응답을_생성할_수_있다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();

        //when
        MyProfileResponse result = MyProfileResponse.from(user);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getNickname()).isEqualTo("gooch123");
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
