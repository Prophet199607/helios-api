package com.helios.api.config;

import com.helios.api.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/auth/authenticate").permitAll())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/register").permitAll())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/patient/**").authenticated())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/job-type/**").authenticated())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/diagnosis/**").authenticated())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/appointment/**").authenticated())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("api/v1/dashboard/**").authenticated())
//                .authorizeHttpRequests((authorize) -> authorize.requestMatchers( "api/v1/auth/authenticate").permitAll())
//                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/**").authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
//                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOrigins("*")
                        .allowCredentials(false);
            }
        };
    }

}
