package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.model.OneTimePassword;
import com.model.UserEntity;

@Service
public interface OtpRepository  extends JpaRepository<OneTimePassword,Integer> {

    OneTimePassword findByOtp(String otp);
    
    OneTimePassword findByEntity(UserEntity entity);
    

}
