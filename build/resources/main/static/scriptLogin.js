document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        const data = await response.json();
        const token = data.jwt; // Aqui você está pegando o token corretamente do campo "jwt"

        // Armazene o token no localStorage
        localStorage.setItem("token", token);

        // Redireciona para a página de perfil
        window.location.href = "/profile.html";
    } else {
        alert("Erro ao fazer login. Verifique suas credenciais.");
    }
});