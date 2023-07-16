package com.example.polzunovfeastserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment env;

    @Value("${spring.h2.console.path:null}")
    private String h2ConsolePath;

    //TODO configure cors before prod
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/signin", "/user/signup").permitAll()
                        .requestMatchers(GET, "/place", "/place/{id}").permitAll()
                        .requestMatchers(GET, "/event", "/event/{id}").permitAll()
                        //TODO remove below permissions when admins added
                        .requestMatchers(POST, "/place").permitAll()
                        .requestMatchers(PUT, "/place").permitAll()
                        .requestMatchers(DELETE, "/place/{id}").permitAll()
                        .requestMatchers(POST, "/event").permitAll()
                        .requestMatchers(PUT, "/event").permitAll()
                        .requestMatchers(DELETE, "/event/{id}").permitAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        //configuration for h2 console
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            http
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                    )
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers(new AntPathRequestMatcher(h2ConsolePath + "/**")).permitAll()
                    );
        }

        http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
