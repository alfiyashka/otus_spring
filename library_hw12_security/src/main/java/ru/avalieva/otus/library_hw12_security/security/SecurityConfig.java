package ru.avalieva.otus.library_hw12_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.avalieva.otus.library_hw12_security.service.MessageService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth,
                                             MessageService messageService) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder(messageService));
    }

    @Override
    public void configure( WebSecurity web ) {
        web.ignoring().antMatchers( "/", "/newuser", "/api/users");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/books/", true)
                .and()
                .logout();
    }

    @Bean
    public PasswordEncoder passwordEncoder(MessageService messageService) {
        return new MD5PasswordEncoder(messageService);
    }

}