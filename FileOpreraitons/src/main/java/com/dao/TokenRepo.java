package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Integer> {

    Token findByToken(String token);

}
