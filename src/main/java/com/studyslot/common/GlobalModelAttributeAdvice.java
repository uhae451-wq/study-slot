package com.studyslot.common;

import com.studyslot.user.entity.User;
import com.studyslot.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// @Controller(화면용)에서 렌더링되는 모든 페이지에 "loginUser"를 자동으로 넣어줌
// @RestController에는 적용되지 않음 (뷰가 없어서)
@ControllerAdvice
public class GlobalModelAttributeAdvice {

    private final UserRepository userRepository;

    public GlobalModelAttributeAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("loginUser")
    public User addLoginUserToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 비로그인 상태면 principal이 없거나 우리가 세팅한 형태가 아님
        if (authentication == null || !(authentication.getPrincipal() instanceof Long)) {
            return null;
        }

        Long userId = (Long) authentication.getPrincipal();

        // 혹시 탈퇴했거나 삭제된 유저면 null 처리 (예외로 화면이 깨지지 않게)
        return userRepository.findById(userId).orElse(null);
    }
}