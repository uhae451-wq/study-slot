document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("login-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const errorEl = document.getElementById("login-error");
    const submitBtn = document.getElementById("submit-btn");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        errorEl.textContent = "";

        const email = emailInput.value.trim();
        const password = passwordInput.value;

        if (!email || !password) {
            errorEl.textContent = "이메일과 비밀번호를 모두 입력해주세요.";
            return;
        }

        submitBtn.disabled = true;
        submitBtn.textContent = "로그인 중...";

        try {
            const response = await fetch("/user/login", {
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

            // 로그인 성공 -> 토큰을 저장해두고 다음 요청부터 사용
            localStorage.setItem("token", data.token);
            localStorage.setItem("nickname", data.nickname);

            alert(`${data.nickname}님 환영합니다!`);
            window.location.href = "/";   // 로그인 후 이동할 페이지 (아직 없으면 우선 홈으로)

        } catch (err) {
            errorEl.textContent = "서버와 통신 중 문제가 발생했어요.";
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = "로그인";
        }
    });
});