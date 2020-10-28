package com.securityconfig;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

@Service
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;

    @Autowired
    private LocaleResolver localeResolver;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // TODO Auto-generated method stub
        setDefaultFailureUrl("/SignupAndLogin");
        super.onAuthenticationFailure(request, response, exception);
        final Locale locale = localeResolver.resolveLocale(request);
//        String errorMessage = "in correct details";
        String errorMessage = "in correct details";

        if (exception.getMessage()
                .equalsIgnoreCase("blocked")) {
//                errorMessage = messages.getMessage("auth.message.blocked", null, locale);
                    errorMessage = "user blocked";
        }   
        HttpSession session = request.getSession(false);
        System.out.println(request);
        if (session!=null) {
            session
            .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage); 
        }
        
    }
}
