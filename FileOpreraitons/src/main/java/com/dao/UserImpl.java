package com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.UserEntity;

@Service
public class UserImpl {

    @Autowired
    UserRepo repo;
    
//    @Autowired
//    private BCryptPasswordEncoder passenc;
    
    public UserEntity saveUser(UserEntity entity) {
        UserEntity userEntity = null;
//        entity.setPass(passenc.encode(entity.getPass()));
        try {
            userEntity = repo.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    public UserEntity getUserByEmail(String email) {
        UserEntity findByEmail = null;
        try {
            findByEmail = repo.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findByEmail;
    }

    public UserEntity getUserByMobile(String mobile) {
        UserEntity entity=null;
        try {
            entity =  repo.findByMobile(mobile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }
    
    public UserEntity getUserByIdWithoutEnable(int id) {
        UserEntity entity=null;
        try {
            entity =  repo.findByIdWithoutEnable(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }

    public void deleteUserByMobileWithoutEnable(String mobile) {
        try {
            repo.deleteUserByMobileWithoutEnable(mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findByEmailOrMobile(String mobile,String email) {
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
    
    public UserEntity findByEmailWithoutEnable(String email) {
        UserEntity f=null;
        try {
          f =  repo.findByEmailWithoutEnable(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
    
    public UserEntity findByMobileWithoutEnable(String mobile) {
        UserEntity entity=null;
        try {
            entity =  repo.findByMobileWithoutEnable(mobile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }
}
