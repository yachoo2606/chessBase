package pl.tiwpr.chessbase.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.tiwpr.chessbase.exceptions.CustomAccessDeniedHandler;
import pl.tiwpr.chessbase.exceptions.CustomJwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomJwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .cors(cors -> cors.disable())
        .csrf( csrf -> csrf.disable())
        .authorizeHttpRequests()
        .requestMatchers("/**").permitAll();
//        .requestMatchers("/tokens/**").permitAll()
//        .requestMatchers(HttpMethod.POST, "/users").permitAll()
//        .requestMatchers(HttpMethod.GET, "/players").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .exceptionHandling()
//        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//        .accessDeniedHandler(new CustomAccessDeniedHandler())
//        .and()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .authenticationProvider(authenticationProvider)
//        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
