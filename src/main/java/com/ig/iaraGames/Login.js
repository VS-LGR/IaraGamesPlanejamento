app.get('/api', async (req, res) => {
    const { username, password } = req.body;

    // Busca o usuário no banco de dados
    const user = await User.findOne({ where: { username } });

    if (!user) {
        return res.status(401).json({ message: 'Usuário não encontrado' });
    }

    // Verifica se a senha está correta
    const isPasswordValid = await bcrypt.compare(password, user.password); // Assumindo que você está usando bcrypt para hash de senha

    if (!isPasswordValid) {
        return res.status(401).json({ message: 'Senha incorreta' });
    }

    // Senha válida, autentica o usuário (retorne um token ou uma resposta de sucesso)
    const token = jwt.sign({ userId: user.id }, 'your_secret_key'); // Exemplo de uso de JWT
    return res.status(200).json({ message: 'Login bem-sucedido', token });
});