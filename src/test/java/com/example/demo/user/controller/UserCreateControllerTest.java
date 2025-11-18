package com.example.demo.user.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void UserCreate를_통해_PENDING상태의_사용자를_생성할_수_있다() {
        //given
        TestContainer testContainer = new TestContainer(
                new TestClockHolder(307L),
                new TestUuidHolder("test-uuid")
        );
        UserCreate userCreate = UserCreate.builder()
                .email("gooch123@naver.com")
                .nickname("gooch123")
                .address("Daegu")
                .build();

        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);
        //then
        assertThat(result.getStatusCode().value()).isEqualTo(201);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("gooch123@naver.com");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getNickname()).isEqualTo("gooch123");
    }

}
