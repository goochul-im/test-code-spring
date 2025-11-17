package com.example.demo.user.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build());

        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getNickname()).isEqualTo("gooch123");
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );

        UserController userController = testContainer.userController;

        //when & then
        assertThatThrownBy(() -> userController.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화할_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build());

        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa");

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(302);
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한없음_에러를_받는다() throws Exception {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build());

        //when
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_가져올_때_개인정보인_주소도_갖고_올_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("gooch123@naver.com");

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getNickname()).isEqualTo("gooch123");
        assertThat(result.getBody().getAddress()).isEqualTo("Daegu");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(307L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa-aaaaaaaaaa-aaaaaaaaa-aaaaaaaaaaa")
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
                "gooch123@naver.com",
                new UserUpdate(
                        "new-gooch123",
                        "Neo-Daegu"
                )
        );

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getNickname()).isEqualTo("new-gooch123");
        assertThat(result.getBody().getAddress()).isEqualTo("Neo-Daegu");
    }

}
