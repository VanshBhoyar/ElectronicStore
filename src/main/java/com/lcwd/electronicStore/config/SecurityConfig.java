package com.lcwd.electronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails admin = User.builder()
                .username("vansh")
                .password(passwordEncoder().encode("vansh123"))
                .roles("ADMIN")
                .build();

        UserDetails normal = User.builder()
                .username("sam")
                .password(passwordEncoder().encode("sam123"))
                .roles("NORMAL")
                .build();

//        UserDetailsService IS INTERFACE SO we use InMemoryUserDetailsManager bcz it is implementation
        return new InMemoryUserDetailsManager(admin,normal);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
