package com.ocdev.biblio.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired CustomAuthenticationProvider authenticationProvider;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
		        .authorizeRequests()
			        .antMatchers("/").permitAll()
			        .antMatchers("/login**").permitAll()
			        .antMatchers("/checklogin").permitAll()
			        .antMatchers("/inscription").permitAll()
			        .antMatchers("/enregistrerUtilisateur").permitAll()
			        .antMatchers("/theme/error").permitAll()
			        .antMatchers("/theme/fatal").permitAll()
			        .antMatchers("/public/**").permitAll()
			        .antMatchers("/webjars/**").permitAll()
			        .anyRequest().authenticated()
		        .and()
        		.formLogin()
	                .loginPage("/login")
	                .defaultSuccessUrl("/abonne/listeOuvrages")
	                .permitAll()
	                .and()
	                .logout()
	                .invalidateHttpSession(true)
	                .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
    	auth.authenticationProvider(authenticationProvider);
    }
}