package com.example.demo.user.domain;

import com.example.demo.common.service.UuidHolder;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.UUID;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate, UuidHolder uuidHolder) {
        return User.builder()
                .email(userCreate.getEmail())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .nickname(userCreate.getNickname())
                .certificationCode(uuidHolder.random())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(id)
                .email(email)
                .address(userUpdate.getAddress())
                .status(status)
                .nickname(userUpdate.getNickname())
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User login(ClockHolder clockHolder) {
        return User.builder()
                .id(id)
                .email(email)
                .address(address)
                .status(status)
                .nickname(nickname)
                .certificationCode(certificationCode)
                .lastLoginAt(clockHolder.mills())
                .build();
    }

    public User certification(String certificationCode) {
        if (!certificationCode.equals(this.certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(id)
                .email(email)
                .address(address)
                .status(UserStatus.ACTIVE)
                .nickname(nickname)
                .certificationCode(certificationCode)
                .lastLoginAt(Clock.systemUTC().millis())
                .build();
    }

}
