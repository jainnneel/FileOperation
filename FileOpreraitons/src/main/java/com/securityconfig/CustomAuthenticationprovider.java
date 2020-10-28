package com.securityconfig;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dao.LoginAttemptService;
import com.dao.RegUserImpl;
import com.model.RegUser;

@Service
public class CustomAuthenticationprovider implements AuthenticationProvider  {
    
    @Autowired
    private RegUserImpl userImpl;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        UsernamePasswordAuthenticationToken authenticationToken = null;
        String ip = getClientIP();
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
       
        if (loginAttemptService.isBlocked(ip)) {
            new java.util.Timer().schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            loginAttemptService.loginSucceeded(ip);
                        }
                    }, 
                    1000*60*10 
            );
          
            throw new RuntimeException("Try After 10 minutes");
        }
        RegUser entity = userImpl.getUserByEmailOrMobile(username);
        
        if(entity!=null) {
            if(entity.getEmail().equals(username) || entity.getMobile().equals(username)) {
                if (password.equals(entity.getPass())) {
                    List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
                    grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
                    authenticationToken = new UsernamePasswordAuthenticationToken(new User(username, password, grantedAuths), password,grantedAuths);
                }
            }
        }else {
            throw new UsernameNotFoundException("not valid credentials");
        }
       return authenticationToken;
    }
    
   
    
    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
    
}
