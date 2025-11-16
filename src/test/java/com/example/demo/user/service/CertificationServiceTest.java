package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

    @Test
    void 이메일_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다(){
        //given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(mailSender);

        //when
        certificationService.send("kok2@naver.com",1, "proper-code");

        //then
        Assertions.assertThat(mailSender.email).isEqualTo("kok2@naver.com");
        Assertions.assertThat(mailSender.title).isEqualTo("Please certify your email address");
        Assertions.assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: " + certificationService.generateCertificationUrl(1, "proper-code"));
    }

}
