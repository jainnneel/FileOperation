package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.model.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Integer> {

    @Query(value="from UserEntity where email=?1 and isEnable=1")
    UserEntity findByEmail(String email);

    @Query(value="from UserEntity where mobile=?1 and isEnable=1")
    UserEntity findByMobile(String mobile);

    @Query(value="from UserEntity where mobile=?1 OR email=?2 and isEnable=1")
    UserEntity findByEmailOrMobile(String mobile,String email);
   
    @Query(value="from UserEntity where id=?1 and isEnable=0")
    UserEntity findByIdWithoutEnable(int id);

    @Query(value="from UserEntity where email=?1 and isEnable=0")
    UserEntity findByEmailWithoutEnable(String email);
    
    @Query(value="delete from UserEntity where mobile=?1 and isEnable=0")
    void deleteUserByMobileWithoutEnable(String mobile);
    
    @Query(value="from UserEntity where mobile=?1 and isEnable=0")
    UserEntity findByMobileWithoutEnable(String mobile);

}
