package com.example.demo.user.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void User는_UserCreate객체로_생성할_수_있다(){
        //given
        UserCreate dto = UserCreate.builder()
                .email("gooch123@naver.com")
                .address("Daegu")
                .nickname("gooch123")
                .build();

        //when
        User result = User.from(dto, new TestUuidHolder("aaaaa-aaaaaa-aaaaaa-aaaaaaaaa"));

        //then
        assertThat(result.getId()).isNull();
        assertThat(result.getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getNickname()).isEqualTo("gooch123");
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaaaaa-aaaaaa-aaaaaaaaa");
    }

    @Test
    void User는_UserUpdate객체로_데이터를_업데이트_할_수_있다(){
        //given
        User writer = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();
        UserUpdate dto = UserUpdate.builder()
                .address("Seoul")
                .nickname("gooch1774")
                .build();

        //when
        User result = writer.update(dto);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getNickname()).isEqualTo("gooch1774");
        assertThat(result.getAddress()).isEqualTo("Seoul");
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa");
    }

    @Test
    void User는_로그인을_할수있고_마지막_로그인_시간이_변경된다(){
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
        User result = user.login(new TestClockHolder(302L));

        //then
        assertThat(result.getLastLoginAt()).isEqualTo(302L);
    }

    @Test
    void User는_인증_코드로_계정을_활성화_할_수_있다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();

        //when
        User result = user.certification("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa");

        //then
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 잘못된_인증_코드로_계정을_활성화_하면_에러를_던진다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build();

        //when & then
        assertThatThrownBy(() -> user.certification("wrong-code")).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}
