package com.dao;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.model.RegUser;

@Service
public class RegUserImpl implements UserDetailsService{

    @Autowired
    UserRegRepo repo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String ip = getClientIP();
//        System.out.println(ip+"sdsddsddssssssssssssssd");
//        if (loginAttemptService.isBlocked(ip)) {
//            throw new RuntimeException("blocked");
//        }
        RegUser entity = getUserByEmailOrMobile(username);
        return new User(entity.getEmail(), entity.getPass(), true, true, true, entity.isEnable(),new ArrayList<>());
    }
    
    public RegUser getUserByEmailOrMobile(String username) {
        RegUser findByEmailOrMobile = null;
        try {
            findByEmailOrMobile = repo.findByMobile(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findByEmailOrMobile;
    }
    
    public void createUser(RegUser regEntity) {
        try {
            repo.save(regEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findByEmailOrMobile(String mobile, String email) {
        boolean f=false;
        try {
            if(repo.findByEmailOrMobile(mobile,email)!=null) {
                f=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
