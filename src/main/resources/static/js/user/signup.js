// 이메일, 닉네임 검증용 정규식 (통과/실패 단일 판정)
const PATTERNS = {
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    nickname: /^[가-힣a-zA-Z0-9]{2,10}$/
};

const MESSAGES = {
    email: {
        valid: "사용 가능한 이메일 형식이에요.",
        invalid: "이메일 형식이 올바르지 않아요. (예: test@example.com)"
    },
    nickname: {
        valid: "사용 가능한 닉네임이에요.",
        invalid: "한글/영문/숫자 2~10자로 입력해주세요. (공백, 특수문자 불가)"
    }
};

// 비밀번호는 조건을 세부적으로 쪼개서 각각 판정
const PASSWORD_REQUIREMENTS = {
    length: /^.{8,20}$/,
    letter: /[a-zA-Z]/,
    number: /[0-9]/
};

// 비밀번호 조건 체크 + 항목별 초록불 토글
function checkPasswordRequirements(value) {
    const results = {};

    for (const [key, pattern] of Object.entries(PASSWORD_REQUIREMENTS)) {
        results[key] = pattern.test(value);

        const li = document.getElementById(`req-${key}`);
        if (li) {
            li.classList.toggle("satisfied", results[key]);
        }
    }

    return Object.values(results).every(Boolean);
}

// 이메일/닉네임처럼 통과-실패 단일 메시지로 보여주는 필드용
function validateSimpleField(fieldName, inputEl, msgEl) {
    const value = inputEl.value.trim();
    const pattern = PATTERNS[fieldName];
    const isValid = pattern.test(value);

    if (value.length === 0) {
        inputEl.classList.remove("valid", "invalid");
        msgEl.textContent = "";
        msgEl.classList.remove("valid", "invalid");
        return false;
    }

    inputEl.classList.toggle("valid", isValid);
    inputEl.classList.toggle("invalid", !isValid);
    msgEl.textContent = isValid ? MESSAGES[fieldName].valid : MESSAGES[fieldName].invalid;
    msgEl.classList.toggle("valid", isValid);
    msgEl.classList.toggle("invalid", !isValid);

    return isValid;
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signup-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const nicknameInput = document.getElementById("nickname");
    const submitBtn = document.getElementById("submit-btn");

    const emailMsg = document.getElementById("email-msg");
    const nicknameMsg = document.getElementById("nickname-msg");

    function isPasswordValid() {
        return checkPasswordRequirements(passwordInput.value);
    }

    function checkAllValid() {
        const emailValid = PATTERNS.email.test(emailInput.value.trim());
        const nicknameValid = PATTERNS.nickname.test(nicknameInput.value.trim());
        const passwordValid = isPasswordValid();

        const allValid = emailValid && nicknameValid && passwordValid;
        submitBtn.disabled = !allValid;
        return allValid;
    }

    // 이메일 실시간 검사
    emailInput.addEventListener("input", () => {
        validateSimpleField("email", emailInput, emailMsg);
        checkAllValid();
    });

    // 닉네임 실시간 검사
    nicknameInput.addEventListener("input", () => {
        validateSimpleField("nickname", nicknameInput, nicknameMsg);
        checkAllValid();
    });

    // 비밀번호 실시간 검사 (조건별 체크리스트)
    passwordInput.addEventListener("input", () => {
        const valid = isPasswordValid();
        passwordInput.classList.toggle("valid", valid && passwordInput.value.length > 0);
        passwordInput.classList.toggle("invalid", !valid && passwordInput.value.length > 0);
        checkAllValid();
    });

    // 처음 로드 시 버튼 비활성화 상태로 시작
    submitBtn.disabled = true;

    // 폼 제출 시 최종 검증 (혹시 모를 우회 방지)
    form.addEventListener("submit", (e) => {
        const emailValid = validateSimpleField("email", emailInput, emailMsg);
        const nicknameValid = validateSimpleField("nickname", nicknameInput, nicknameMsg);
        const passwordValid = isPasswordValid();

        if (!emailValid || !nicknameValid || !passwordValid) {
            e.preventDefault();
        }
    });
});