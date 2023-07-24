package com.example.userservice.security;


import com.example.userservice.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.servlet.configuration.WebMvcSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig  {

    private final BCryptPasswordEncoder passwordEncoder;

    private final Environment environment;

    private final UserService userService;


    private final AuthenticationConfiguration authenticationConfiguration;

    private final AuthenticationManagerBuilder builder;

//    private static final String[] WHITE_LIST = {
//
//    };
//
//    private static final String[] USER_LIST = {
//            "/welcome",
//            "/health-check",
//            "/users/**",
//            "/**"
//    };



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/actuator/**").permitAll())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/**").permitAll())
                .addFilter(customAuthenticationProcessingFilter())
                .getOrBuild();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter customAuthenticationProcessingFilter() throws Exception{
        AuthenticationFilter filter = new AuthenticationFilter(userService,environment);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }




}
