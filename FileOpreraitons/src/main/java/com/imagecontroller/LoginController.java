package com.imagecontroller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dao.RegUserImpl;
import com.securityconfig.JwtUtil;

//@Controller
public class LoginController {
//
//    @Autowired
//    RegUserImpl userservice;
//    
//    @Autowired
//    JwtUtil jwtutil;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    
//   
//    @PostMapping(value="/loginss")
//    public String loginProcess(@RequestParam("uname") String uname,@RequestParam("pass") String pass, HttpServletResponse response,HttpServletRequest request ) throws Exception {
//        System.out.println("Dasdsadsadsa");
//        authenticate(uname,pass);
//       
//        final UserDetails userDetails = userservice
//                .loadUserByUsername(uname);
//
//        final String token = jwtutil.generateToken(userDetails);
//            System.out.println("dsadad");
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));        
//            headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
//            headers.add("Authorization", "Bearer "+token );
//            System.out.println(token);
//            System.out.println("dsadad");
//            response.addHeader("Authorization", "Bearer "+token );
//            return "redirect:/home";
//
//    }
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            System.out.println("Dsad");
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (Exception e) {
//            throw new RuntimeException("DSDS");
//        }
//        
//    }
    
    
}
