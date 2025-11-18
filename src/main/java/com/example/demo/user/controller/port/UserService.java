package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

public interface UserService{

    void login(long id);

    void verifyEmail(long id, String certificationCode);

    User create(UserCreate userCreate);

    User getByEmail(String email);

    User getById(long id);

    User update(long id, UserUpdate userUpdate);

}
