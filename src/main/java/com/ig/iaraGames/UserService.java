package com.ig.iaraGames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }



    // Método para excluir o usuário pelo ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Método para registrar um novo usuário
    public User registerUser(User user) {
        // Verifica se o usuário já existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        // Encripta a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Método para autenticar o login do usuário
    public User loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        User user = userOptional.get();

        // Verifica a senha criptografada
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        return user;
    }
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
                        user.setUsername(userDetails.getUsername());
                    }
                    if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
                        user.setEmail(userDetails.getEmail());
                    }
                    if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
                        user.setName(userDetails.getName());
                    }

                    // Apenas encripta e atualiza a senha se for fornecida
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }

                    return userRepository.save(user);
                })
                .orElse(null);
    }
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }
}
