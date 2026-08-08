package com.studyslot.user.controller;

import com.studyslot.user.service.UserService;
import com.studyslot.user.dto.UserSignupRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
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

}
