package com.example.demo.medium;

import com.example.demo.user.domain.User;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Sql("/sql/user-service-test-data.sql")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        String mail = "kok2@gmail.com";

        //when
        User result = userService.getByEmail(mail);

        //then
        assertThat(result.getNickname()).isEqualTo("kok2");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        //given
        String mail = "kok3@gmail.com";

        //when & then
        assertThatThrownBy(() -> userService.getByEmail(mail)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        //when
        User result = userService.getById(1);

        //then
        assertThat(result.getNickname()).isEqualTo("kok2");
    }

    @Test
    void getById는_PENDING_상태인_유저는_찾아올_수_없다() {
        //when & then
        assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void UserCreateDto를_이용해_유저를_생성할_수_있다() {
        //given
        UserCreate dto = UserCreate.builder()
                .email("kok4@gmail.com")
                .address("Daegu")
                .nickname("kok2-2")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(dto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void UserUpdateDto를_이용해_유저를_수정할_수_있다() {
        //given
        UserUpdate dto = UserUpdate.builder()
                .address("Daegu")
                .nickname("kok45")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        userService.update(1, dto);

        //then

        User result = userService.getById(1);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getNickname()).isEqualTo("kok45");
        assertThat(result.getAddress()).isEqualTo("Daegu");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변한다() {
        //given
        //when
        userService.login(1);

        //then

        User result = userService.getById(1);
        assertThat(result.getLastLoginAt()).isGreaterThan(1); //FIX
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(2, "aaaaaaaaaaaaaa-aaaaaaaaa-aaaaaaaa-aaaaaaaaa");

        //then

        User result = userService.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        //given
        //when & then
        assertThatThrownBy(() -> userService.verifyEmail(2, "aaaaaaaaaaaaaa-aaaaaaaaa-aaaaaaaa-aaaaaa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}
