package com.studyslot.user.controller;

import com.studyslot.common.jwt.JwtTokenProvider;
import com.studyslot.user.dto.LoginRequest;
import com.studyslot.user.entity.User;
import com.studyslot.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("message", firstError));
        }

        try {
            User user = userService.login(loginRequest);
            String token = jwtTokenProvider.createToken(user.getId(), user.getEmail());
            System.out.println("발급된 토큰: " + token);   // 임시 디버깅용
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "nickname", user.getNickname()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}