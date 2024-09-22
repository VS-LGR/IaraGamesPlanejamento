package com.ig.iaraGames;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**", "/resources/**").permitAll()
                        .requestMatchers("/api/app_user/register", "/api/auth/login", "/api/app_user/profile").permitAll()
                        .requestMatchers("/api/games/register", "/api/games/update", "/api/games/delete").permitAll()

                        .requestMatchers("/profile.html", "/register.html", "/scriptLogin.js", "/scriptCadastro.js", "/scriptProfile.js", "/styleProfile.css", "/loginstyle.css", "/cadstyle.css", "/gaming.svg", "/cadstyles.css", "/cad.html","/edicaostyles.css", "/edicao.html", "/exclusao.html", "/exclusaostyles.css", "/IaraLogin.svg").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Unauthorized");
                        })
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login.html?logout")
                        .permitAll()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin();  // Permitir carregamento do console H2 em iframe

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}