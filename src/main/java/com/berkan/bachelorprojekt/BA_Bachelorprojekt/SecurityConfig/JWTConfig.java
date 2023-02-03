package com.berkan.bachelorprojekt.BA_Bachelorprojekt.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/*

@Configuration
@EnableWebSecurity
public class JWTConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userService;

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
        return NoOpPasswordEncoder.getInstance();
    }


    // überschreiben der Authorization
    @Override
    protected void configure(HttpSecurity http) {
        try {
            http
//                    .cors().and()
                    .csrf().disable()
//                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                    .authorizeRequests()
//                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/xrtool/*").hasRole("USER")
                    .antMatchers("/advanced_search").hasRole("USER")
                    .antMatchers("/creation").permitAll()
                    .antMatchers("/edit").permitAll()
                    .antMatchers("/authenticate").permitAll()
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated().and()
                    .httpBasic();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
*/
