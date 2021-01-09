package com.ocdev.biblio.apibiblio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired UserDetailsService userDetailsService;
    @Autowired ApiBiblioAuthenticationEntryPoint apiBiblioAuthenticationEntryPoint;
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
    	httpSecurity.csrf().disable();//Pour ne plus utiliser les sessions
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//Pour ne plus utiliser les sessions
		httpSecurity.authorizeRequests()
		.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/webjars/**", "/swagger-ui.html").permitAll();
		httpSecurity.authorizeRequests().antMatchers("/api/v1/utilisateurs/**").permitAll();
		httpSecurity.authorizeRequests().anyRequest().authenticated();
		httpSecurity.httpBasic().authenticationEntryPoint(apiBiblioAuthenticationEntryPoint);
		httpSecurity.formLogin().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Création du Manager d'Authentification 
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
