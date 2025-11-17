package com.example.demo.mock;

import com.example.demo.common.service.UuidHolder;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.mailSender = new FakeMailSender();
        this.certificationService = new CertificationService(mailSender);
        this.postService = PostServiceImpl.builder()
                .clockHolder(clockHolder)
                .userRepository(userRepository)
                .postRepository(postRepository)
                .build();
        UserServiceImpl userService = UserServiceImpl.builder()
                .certificationService(certificationService)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .build();

        this.userReadService = userService;
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                .userReadService(userReadService)
                .userCreateService(userCreateService)
                .userUpdateService(userUpdateService)
                .authenticationService(authenticationService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(userCreateService)
                .build();
        this.postController = PostController.builder()
                .postService(postService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postService(postService)
                .build();
    }

}
