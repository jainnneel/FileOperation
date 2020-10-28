package com.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private RegUserImpl userService; 
//    
//    @Autowired
//    private BCryptPasswordEncoder passenc;
    
    @Autowired
    private CustomAuthenticationprovider customAuthenticationprovider;
//    @Autowired
//    private JwtFilter filter;
//    
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationprovider);   
//        }
//    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationprovider);
    }
    
    
//
//    @Bean
//    DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider d = new  DaoAuthenticationProvider();
//        d.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        d.setUserDetailsService(userService);
//        return d;
//    }
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    
    @Autowired
    CustomAuthenticationFailureHandler authenticationFailureHandler;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
        .authorizeRequests()
        .antMatchers("/SignupAndLogin").permitAll()
        .antMatchers("/home").permitAll()
        .antMatchers("/proccessing/**").authenticated()
        .and()
//        .addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class)
        
        .csrf().disable()
         .formLogin()
           .loginPage("/SignupAndLogin").permitAll()
           .loginProcessingUrl("/login")
           .failureHandler(authenticationFailureHandler)
           .defaultSuccessUrl("/home")
           .and()
           .logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .clearAuthentication(true)
        .logoutRequestMatcher( new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/SignupAndLogin?logout").permitAll()
    .and()
        .rememberMe()
    .and()
        .sessionManagement()                          
        .maximumSessions(1)                         
        .maxSessionsPreventsLogin(false)          
        .expiredUrl("/login?expired");  
        
    }
 
}
