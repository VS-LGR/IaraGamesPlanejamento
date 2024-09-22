document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/api/app_user/profile", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });

        if (response.ok) {
            const userData = await response.json();
            document.getElementById("username").innerText = userData.username;
            document.getElementById("email").innerText = userData.email;
            document.getElementById("name").innerText = userData.name;
        } else {
            alert("Erro ao carregar o perfil.");
            if (response.status === 401) {
                localStorage.removeItem("token");
                window.location.href = "login.html";
            }
        }
    } catch (error) {
        console.error("Erro na requisição de perfil:", error);
        alert("Ocorreu um erro ao carregar o perfil.");
    }
});

// Atualizar perfil do usuário
document.getElementById("updateProfileForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const updatedProfile = {
        username: document.getElementById("newUsername").value || null,
        email: document.getElementById("newEmail").value || null,
        name: document.getElementById("newName").value || null,
        password: document.getElementById("newPassword").value || null // Senha opcional
    };

    const token = localStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8080/api/app_user/profile", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(updatedProfile)
        });

        if (response.ok) {
            const data = await response.json();

            // Atualize o token no localStorage
            localStorage.setItem("token", data.jwt);

            alert("Perfil atualizado com sucesso!");

            // Atualiza os dados exibidos após a atualização
            document.getElementById("username").innerText = updatedProfile.username || document.getElementById("username").innerText;
            document.getElementById("email").innerText = updatedProfile.email || document.getElementById("email").innerText;
            document.getElementById("name").innerText = updatedProfile.name || document.getElementById("name").innerText;
        } else {
            alert("Erro ao atualizar o perfil.");
        }
    } catch (error) {
        console.error("Erro na atualização do perfil:", error);
        alert("Ocorreu um erro ao atualizar o perfil.");
    }
});



// JavaScript/jQuery para capturar o clique no botão deleteAccount
$(document).ready(function() {
    $('#deleteAccount').click(function() {
        if (confirm("Tem certeza que deseja excluir sua conta? Esta ação é irreversível.")) {
            // Faz a requisição de exclusão da conta
            $.ajax({
                url: '/api/app_user/profile', // Endpoint que exclui a conta
                type: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('jwtToken') // Envia o JWT no cabeçalho
                },
                success: function(response) {
                    alert("Conta excluída com sucesso.");
                    // Redireciona ou realiza outra ação após a exclusão
                    window.location.href = "/login.html"; // Exemplo de redirecionamento após exclusão
                },
                error: function(xhr, status, error) {
                    alert("Erro ao excluir a conta: " + xhr.responseJSON.error);
                }
            });
        }
    });
});


