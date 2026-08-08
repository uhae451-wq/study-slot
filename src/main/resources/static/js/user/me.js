document.addEventListener("DOMContentLoaded", async () => {

    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/user/login";
        return;
    }

    try {
        const response = await fetch("/user/my-info", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const data = await response.json();

        if (!response.ok) {
            localStorage.removeItem("token");
            window.location.href = "/user/login";
            return;
        }

        document.getElementById("user-info").innerHTML = `
            <h2>${data.nickname}님 환영합니다!</h2>
            <p>이메일: ${data.email}</p>
        `;

    } catch (error) {
        console.error(error);
        document.getElementById("user-info").textContent =
            "서버와 통신 중 문제가 발생했어요.";
    }
});