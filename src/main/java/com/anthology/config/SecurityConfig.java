package com.anthology.config;


import com.anthology.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity)throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {

                    /// publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/get").permitAll();

                    http.requestMatchers(HttpMethod.GET, "/api/songs/").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/albums/").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/artists/").permitAll();

                    /// privados
                    http.requestMatchers("/api/admin/").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/users/").hasRole("ADMIN");

                    http.requestMatchers(HttpMethod.PATCH, "/api/users/").hasAnyRole("ADMIN", "USER");

                    http.requestMatchers(HttpMethod.POST,"/auth/post").hasAnyRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE,"/auth/delete").hasAnyRole("ADMIN");
                    /// defaults
                    http.anyRequest().denyAll();

                })
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
