package com.studyslot.user.controller;

import com.studyslot.common.jwt.JwtTokenProvider;
import com.studyslot.user.dto.LoginRequest;
import com.studyslot.user.entity.User;
import com.studyslot.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("message", firstError));
        }

        try {
            User user = userService.login(loginRequest);
            String token = jwtTokenProvider.createToken(user.getId(), user.getEmail());
            System.out.println("발급된 토큰: " + token);   // 임시 디버깅용
            ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                    .httpOnly(true)
                    .secure(false) // localhost 개발 환경
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Lax")
                    .build();

            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    cookie.toString()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "redirectUrl", "/user/me"
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}