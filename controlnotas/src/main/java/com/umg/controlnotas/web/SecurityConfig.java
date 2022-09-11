package com.umg.controlnotas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Sirve para agregar a los usuarios que hacen login en la aplicación, aquí se autentican
     */
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Sirve para restringir las URLs a los usuarios según su rol, aquí se autorizan los accesos
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//desactivar CSRF https://docs.spring.io/spring-security/reference/features/exploits/csrf.html
                .authorizeHttpRequests()
                .antMatchers("/alumno/**", "/plan-trabajo/**", "/institucion/**").hasRole("DIRECTOR")
                .antMatchers("/").hasAnyRole("DOCENTE", "DIRECTOR")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error/403");
    }
}
