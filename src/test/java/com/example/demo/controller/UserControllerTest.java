package com.example.demo.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/user-controller-test-data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nickname").value("kok2"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_apI_호출할_경우_404_응답을_받는다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/12341"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 12341를 찾을 수 없습니다."));

    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화할_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "aaaaaaaaaaaaaa-aaaaaaaaa-aaaaaaaa-aaaaaaaaa"))
                .andExpect(status().isFound())
                ;

        UserEntity userEntity = userRepository.findById(2L).get();
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }



    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한없음_에러를_받는다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify")
                        .queryParam("certificationCode", "wrong-certification-code"))
                .andExpect(status().isForbidden())
        ;

    }

    @Test
    void 사용자는_내_정보를_가져올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/me")
                        .header("EMAIL","kok2@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("kok2@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("kok2"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("kok2-2")
                .address("Daegu")
                .build();

        //when
        //then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL","kok2@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("kok2@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("kok2-2"))
                .andExpect(jsonPath("$.address").value("Daegu"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

}
