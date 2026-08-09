package com.studyslot.user.controller;

import com.studyslot.common.jwt.JwtTokenProvider;
import com.studyslot.user.dto.LoginRequest;
import com.studyslot.user.entity.User;
import com.studyslot.user.repository.UserRepository;
import com.studyslot.user.service.UserService;
import com.studyslot.user.dto.UserSignupRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String userLoginForm(){
        return "/user/login";
    }


    @GetMapping("/me")
    public String me(
            @AuthenticationPrincipal Long userId,
            Model model
    ) {
        // 필터에서 토큰 검증을 이미 했기 때문에, userId가 null이면 = 인증 안 된 상태
        if (userId == null) {
            return "redirect:/user/login";
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        model.addAttribute("loginUser", user);
        return "user/me";
    }



}
