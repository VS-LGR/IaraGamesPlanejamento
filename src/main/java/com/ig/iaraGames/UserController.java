package com.ig.iaraGames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/app_user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // Obtém todos os usuários (método existente)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Obtém um usuário pelo ID (método existente)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Cria um novo usuário (método existente)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Atualiza um usuário pelo ID (método existente)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deleta um usuário pelo ID (método existente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login do usuário (método existente)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário não encontrado"));
            }

            // Verifica se a senha no pedido corresponde à senha armazenada para o usuário
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas"));
            }

            // Gera um token JWT após uma autenticação bem-sucedida
            String token = jwtUtil.generateToken(existingUser.getUsername());

            // Retorna o token JWT ao usuário
            return ResponseEntity.ok(Map.of("jwt", token, "message", "Login bem-sucedido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // Obtém o perfil do usuário autenticado
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Buscar o usuário completo pelo nome de usuário
        User user = userService.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(user); // Retorna o usuário completo com email, name, etc.
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // Atualizar perfil do usuário autenticado
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(Principal principal, @RequestBody User userDetails) {
        String username = principal.getName();
        User existingUser = userService.findByUsername(username);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("User not found"));
        }

        // Atualize os campos se foram fornecidos
        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
            existingUser.setName(userDetails.getName());
        }

        // Atualize a senha encriptada apenas se o usuário forneceu uma nova senha
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Salva as atualizações
        userService.updateUser(existingUser.getId(), existingUser);

        // Gere um novo token JWT após a atualização
        String newToken = jwtUtil.generateToken(existingUser.getUsername());

        return ResponseEntity.ok(Map.of("message", "User updated successfully", "jwt", newToken));
    }

    // Excluir conta do usuário autenticado (ajustado)
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUserAccount(Authentication authentication) {
        try {
            // Obtém os detalhes do usuário autenticado a partir do token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Busca o usuário no banco de dados
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado"));
            }

            // Exclui o usuário do banco de dados
            userService.deleteUser(user.getId());

            // Opcionalmente, pode-se invalidar o token JWT aqui se você estiver usando um sistema de blacklist

            return ResponseEntity.ok(Map.of("message", "Conta excluída com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
