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
            @CookieValue(value = "accessToken", required = false) String token,
            Model model
    ) {

        if (token == null) {
            return "redirect:/user/login";
        }

        try {
            // JWT 검증 + userId 추출
            Long userId = jwtTokenProvider.getUserId(token);

            // DB에서 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                    );

            model.addAttribute("email", user.getEmail());
            model.addAttribute("nickname", user.getNickname());

            return "user/me";

        } catch (Exception e) {

            // JWT가 잘못됐거나 만료된 경우
            return "redirect:/user/login";
        }
    }



}
