document.addEventListener('DOMContentLoaded', () => {
    const registerUrl = 'http://localhost:8080/api/app_user/register'; // Endpoint de registro
    const userForm = document.getElementById('userFormRegister');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const emailInput = document.getElementById('email');
    const nameInput = document.getElementById('name');

    async function registerUser(event) {
        event.preventDefault();

        const user = {
            username: usernameInput.value,
            password: passwordInput.value,
            email: emailInput.value,
            name: nameInput.value
        };

        try {
            const response = await fetch(registerUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Network response was not ok');
            }

            const data = await response.json();
            alert('Registro bem-sucedido!');
            window.location.href = '/login.html'; // Redireciona para a página de login
        } catch (error) {
            console.error('Failed to register:', error);
            alert('Falha no registro: ' + error.message);
        }
    }

    userForm.addEventListener('submit', registerUser);
});