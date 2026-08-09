package com.studyslot.user.controller;

import com.studyslot.common.jwt.JwtTokenProvider;
import com.studyslot.user.dto.LoginRequest;
import com.studyslot.user.dto.UserEditRequest;
import com.studyslot.user.entity.User;
import com.studyslot.user.repository.UserRepository;
import com.studyslot.user.service.UserService;
import com.studyslot.user.dto.UserSignupRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserRepository userRepository, UserService userService, JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String userSignupForm(Model model){
        model.addAttribute("signupRequest", new UserSignupRequest());
        return "/user/signup";
    }

    @PostMapping("/signup")
    public String userSignup(@Valid @ModelAttribute("signupRequest") UserSignupRequest signupRequest, BindingResult bindingResult, Model model){

        // dto 체크
        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", firstError);
            return "/user/signup";
        }

        // 서비스 조회 중복 체크
        try{
            userService.signup(signupRequest);
        }catch(IllegalAccessError e){
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("signupRequest",signupRequest);
            return "/user/signup";
        }
        return "redirect:/user/signup";
    }

    @GetMapping("/login")
    public String userLoginForm(@RequestParam(required = false) String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        // 쿠키를 "즉시 만료"시켜서 브라우저가 지우게 만듦
        ResponseCookie expiredCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // localhost 개발 환경
                .path("/")
                .maxAge(0)     // 0으로 주면 브라우저가 바로 삭제
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        return "redirect:/space/list";
    }

    @GetMapping("/mypage")
    public String userMypage(@AuthenticationPrincipal Long userId){
        if(userId == null){
            return "redirect:/user/login?redirect=/user/mypage";
        }
        return "/user/mypage";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> userEdit(@AuthenticationPrincipal Long userId,@RequestBody UserEditRequest request) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }
        try {
            userService.editUser(userId, request);
        } catch (IllegalArgumentException | IllegalAccessError e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of(
                "message", "회원정보가 수정되었습니다.",
                "nickname", request.getNickname()
        ));
    }



}
