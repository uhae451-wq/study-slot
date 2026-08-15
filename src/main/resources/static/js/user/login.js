document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("login-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const errorEl = document.getElementById("login-error");
    const submitBtn = document.getElementById("submit-btn");
    const redirectInput = document.getElementById("redirect");

    const params = new URLSearchParams(window.location.search);
    if (params.get('needSignup') === 'true') {
        alert('회원가입(로그인)이 필요합니다.');
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        errorEl.textContent = "";

        const email = emailInput.value.trim();
        const password = passwordInput.value;
        const redirect = redirectInput ? redirectInput.value : "";

        if (!email || !password) {
            errorEl.textContent = "이메일과 비밀번호를 모두 입력해주세요.";
            return;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = "로그인 중...";

        // redirect 값이 있으면 쿼리 파라미터로 붙여서 서버에 전달
        const url = "/user/login" + (redirect ? "?redirect=" + encodeURIComponent(redirect) : "");

        try {
            const response = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (!response.ok) {
                // 401, 400 등 실패 응답 -> 서버가 보내준 메시지 그대로 표시
                errorEl.textContent = data.message || "로그인에 실패했어요.";
                return;
            }

            /*            // 로그인 성공 -> 토큰을 저장해두고 다음 요청부터 사용
                        localStorage.setItem("token", data.token);
                        localStorage.setItem("nickname", data.nickname);*/

            window.location.href = data.redirectUrl;

        } catch (err) {
            errorEl.textContent = "서버와 통신 중 문제가 발생했어요.";
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = "로그인";
        }
    });
});