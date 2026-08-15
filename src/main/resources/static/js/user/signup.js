// 이메일, 닉네임 검증용 정규식
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
    const passwordConfirmInput = document.getElementById("new-password-confirm");
    const nicknameInput = document.getElementById("nickname");
    const submitBtn = document.getElementById("submit-btn");

    const emailMsg = document.getElementById("email-msg");
    const nicknameMsg = document.getElementById("nickname-msg");
    const passwordConfirmMsg = document.getElementById("password-confirm-msg");

    function checkAllValid() {
        const emailValid = PATTERNS.email.test(emailInput.value.trim());
        const nicknameValid = PATTERNS.nickname.test(nicknameInput.value.trim());
        const passwordValid = checkPasswordRequirements(passwordInput.value);
        const confirmValid = checkPasswordMatch(passwordInput, passwordConfirmInput, passwordConfirmMsg);

        const allValid = emailValid && nicknameValid && passwordValid && confirmValid;
        submitBtn.disabled = !allValid;
        return allValid;
    }

    emailInput.addEventListener("input", () => {
        validateSimpleField("email", emailInput, emailMsg);
        checkAllValid();
    });

    nicknameInput.addEventListener("input", () => {
        validateSimpleField("nickname", nicknameInput, nicknameMsg);
        checkAllValid();
    });

    passwordInput.addEventListener("input", () => {
        const valid = checkPasswordRequirements(passwordInput.value);
        passwordInput.classList.toggle("valid", valid && passwordInput.value.length > 0);
        passwordInput.classList.toggle("invalid", !valid && passwordInput.value.length > 0);
        checkAllValid();
    });

    passwordConfirmInput.addEventListener("input", () => {
        checkPasswordMatch(passwordInput, passwordConfirmInput, passwordConfirmMsg);
        checkAllValid();
    });

    submitBtn.disabled = true;

    form.addEventListener("submit", (e) => {
        const emailValid = validateSimpleField("email", emailInput, emailMsg);
        const nicknameValid = validateSimpleField("nickname", nicknameInput, nicknameMsg);
        const passwordValid = checkPasswordRequirements(passwordInput.value);
        const confirmValid = checkPasswordMatch(passwordInput, passwordConfirmInput, passwordConfirmMsg);

        if (!emailValid || !nicknameValid || !passwordValid || !confirmValid) {
            e.preventDefault();
        }
    });
});