// /static/js/common/password-validation.js

const PASSWORD_REQUIREMENTS = {
    length: /^.{8,20}$/,
    letter: /[a-zA-Z]/,
    number: /[0-9]/
};

// 비밀번호 조건 체크 + 항목별 체크리스트(li#req-length 등) 초록불 토글
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

// 비밀번호 확인 필드 일치 검증 (실시간)
function checkPasswordMatch(passwordInput, confirmInput, msgEl) {
    const password = passwordInput.value;
    const confirm = confirmInput.value;

    if (!confirm) {
        msgEl.textContent = "";
        msgEl.classList.remove("valid", "invalid");
        return false;
    }

    const isMatch = password === confirm;

    msgEl.textContent = isMatch
        ? "비밀번호가 일치해요."
        : "비밀번호가 일치하지 않습니다.";
    msgEl.classList.toggle("valid", isMatch);
    msgEl.classList.toggle("invalid", !isMatch);

    return isMatch;
}
function checkPasswordnewPassword(passwordInput, passwordConfirmInput){

}