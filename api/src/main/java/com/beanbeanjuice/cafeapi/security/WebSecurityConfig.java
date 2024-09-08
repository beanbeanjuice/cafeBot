package com.beanbeanjuice.cafeapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;
    private final UnauthorizedHandler unauthorizedHandler;

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .exceptionHandling(h -> h.authenticationEntryPoint(unauthorizedHandler))
            .securityMatcher("/**")
            .authorizeHttpRequests(registry -> registry
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/api/v2/cafe/").permitAll()
                    .requestMatchers("/api/v2/cafe/public/**").permitAll()

                    .requestMatchers("/api/v2/cafe/auth/login").permitAll()
                    .requestMatchers("/api/v2/cafe/auth/logout").permitAll()
                    .requestMatchers("/api/v2/cafe/auth/refresh").permitAll()

                    .requestMatchers("/api/v2/cafe/user/register").permitAll()
                    .requestMatchers("/api/v2/cafe/user/all").hasRole("OWNER")
                    .requestMatchers("/api/v2/cafe/admin").hasRole("ADMIN")

                    // cafeBot
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/user/all").hasRole("OWNER")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/user/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v2/cafebot/user/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v2/cafebot/user/").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/birthday/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v2/cafebot/birthday/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v2/cafebot/birthday/").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/donations").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v2/cafebot/donations").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/donations/sent/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/donations/received/").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/v2/cafebot/code/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/code/").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/games").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v2/cafebot/games").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/games/").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v2/cafebot/interactions/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions/sent").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions/sent/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions/received").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v2/cafebot/interactions/received/").hasRole("ADMIN")

                    .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

}
