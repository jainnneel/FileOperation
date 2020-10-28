package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.model.RegUser;

@Repository
public interface UserRegRepo extends JpaRepository<RegUser,Integer> {

    
    @Query(value="from RegUser where mobile=?1 OR email=?1 ")
    RegUser findByMobile(String username);

    @Query(value="from RegUser where mobile=?1 OR email=?2")
    RegUser findByEmailOrMobile(String mobile, String email);

}
