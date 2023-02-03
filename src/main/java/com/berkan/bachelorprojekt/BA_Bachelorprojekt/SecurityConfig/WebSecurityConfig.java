package com.berkan.bachelorprojekt.BA_Bachelorprojekt.SecurityConfig;


import com.berkan.bachelorprojekt.BA_Bachelorprojekt.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;


    // überschreiben der Authentizifierung
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // Unsere eigene Konfiguration für Spring Security
        auth.userDetailsService(userService);
    }


    // Spring Security sucht nach einem bean welches einen PasswordEncoder returned
    // Hier wird dann angegeben wie die Passwörter verschlüsselt sind
    @Bean
    public PasswordEncoder getPasswordEncoder(){

        return new BCryptPasswordEncoder(5);

//        return NoOpPasswordEncoder.getInstance();
    }

    // überschreiben der Authorization
    @Override
    protected void configure(HttpSecurity http) {
        try {
            http
                    .cors().and()
                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                    .authorizeRequests()
//                        .antMatchers(HttpMethod.OPTIONS, "/register").permitAll()
                        .antMatchers("/xrtool/*").permitAll()
                        .antMatchers("/advanced_search").permitAll()
                        .antMatchers("/searchByTitle").permitAll()
//                        .antMatchers("/creation").hasRole("ADMIN")
                        .antMatchers("/creation").hasRole("ADMIN")
                        .antMatchers("/edit").hasRole("ADMIN")
                        .antMatchers("/basicAuth").permitAll()
                        .antMatchers("/register").permitAll()
                        .antMatchers("/adminUpgrade").permitAll()
                        .antMatchers("/**").permitAll()
                    .anyRequest().authenticated().and()
                    .httpBasic();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
