package com.ig.iaraGames;

import com.ig.iaraGames.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login do usuário
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        String jwt = jwtUtil.generateToken(username);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // Atualizar perfil do usuário autenticado
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(Principal principal, @RequestBody User userDetails) {
        String username = principal.getName();
        User existingUser = userService.findByUsername(username);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("User not found"));
        }

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setName(userDetails.getName());

        // Atualiza a senha encriptada se o usuário forneceu uma nova senha
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        userService.updateUser(existingUser.getId(), existingUser);

        // Gere um novo token JWT após a atualização
        String newToken = jwtUtil.generateToken(existingUser.getUsername());

        return ResponseEntity.ok(Map.of("message", "User updated successfully", "jwt", newToken));
    }
}