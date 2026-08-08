package com.studyslot.user.service;

import com.studyslot.common.SecurityConfig;
import com.studyslot.user.dto.LoginRequest;
import com.studyslot.user.dto.UserSignupRequest;
import com.studyslot.user.entity.User;
import com.studyslot.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;   // ← 이게 맞음
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // 회원가입
    public void signup(UserSignupRequest request){

        // null / 공백 체크
        if(!StringUtils.hasText(request.getEmail())){
            throw new IllegalArgumentException("이메일은 필수 입니다.");
        }
        if(!StringUtils.hasText(request.getNickname())){
            throw new IllegalArgumentException("닉네임은 필수 입니다.");
        }
        if(!StringUtils.hasText(request.getPassword())){
            throw new IllegalArgumentException("비밀번호는 필수 입니다.");
        }

        // 조회로 중복 체크
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalAccessError("이미 사용중인 이메일 입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalAccessError("이미 사용 중인 닉네임이에요.");
        }

        String encodeedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                encodeedPassword,
                request.getNickname()
        );

        userRepository.save(user);
    }

    // 로그인 검증: 성공하면 User 반환, 실패하면 예외
    @Transactional(readOnly = true)
    public User login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않아요."));

        // matches()로 입력한 평문과 DB의 해시값을 비교
        // (일부러 "이메일이 없다"와 "비밀번호가 틀렸다"를 구분 안 하고 같은 메시지로 처리 -> 계정 존재 여부 노출 방지)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않아요.");
        }

        return user;
    }

}
