package com.ig.iaraGames;

public class AuthRequest {

    private String username;
    private String password;

    // Construtor
    public AuthRequest() {}

    // Getter para o nome de usuário
    public String getUsername() {
        return username;
    }

    // Setter para o nome de usuário
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter para a senha
    public String getPassword() {
        return password;
    }

    // Setter para a senha
    public void setPassword(String password) {
        this.password = password;
    }
}
