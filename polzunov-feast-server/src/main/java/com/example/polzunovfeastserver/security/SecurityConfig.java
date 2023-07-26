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

import static com.example.polzunovfeastserver.user.entity.Role.*;
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
                .cors(AbstractHttpConfigurer::disable) //TODO configure cors before prod
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        /*
                        Spring will decode jwt with authorities prefixed by 'SCOPE_',
                        e.g. if user send his token he will have authority 'SCOPE_USER'.
                        .hasRole(USER) method will expect user to have 'ROLE_USER' authority, but we'll have 'SCOPE_USER',
                        so I had to use .hasAuthority(USER.asSCOPE) instead of .hasRole(USER)
                        */

                        //no security
                        .requestMatchers(POST, "/user/signin", "/user/signup").permitAll()
                        .requestMatchers(
                                GET,
                                "/event", "/event/{id}",
                                "/place", "place/{id}",
                                "/category", "/category/{id}"
                        ).permitAll()

                        //only users
                        .requestMatchers(PUT, "/user").hasAuthority(USER.asScope())
                        .requestMatchers(DELETE, "/user").hasAuthority(USER.asScope())
                        .requestMatchers(PUT, "/route").hasAuthority(USER.asScope())
                        .requestMatchers(GET, "/route").hasAuthority(USER.asScope())

                        //only admins (including root)
                        .requestMatchers(
                                POST,
                                "/place", "/event", "/category"
                        ).hasAnyAuthority(ADMIN.asScope(), ROOT.asScope())
                        .requestMatchers(
                                PUT,
                                "/place", "/event", "/category"
                        ).hasAnyAuthority(ADMIN.asScope(), ROOT.asScope())
                        .requestMatchers(
                                DELETE,
                                "/place/{id}", "/event/{id}", "/category/{id}"
                        ).hasAnyAuthority(ADMIN.asScope(), ROOT.asScope())

                        //only root
                        .requestMatchers(POST, "/admin/signup").hasAuthority(ROOT.asScope())
                        .requestMatchers(PUT, "/admin/{username}").hasAuthority(ROOT.asScope())
                        .requestMatchers(DELETE, "/admin/{username}").hasAuthority(ROOT.asScope())

                        .requestMatchers("/**").hasAnyAuthority(ROOT.asScope(), USER.asScope(), ADMIN.asScope())
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

        http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
