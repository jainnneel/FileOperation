package com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Token;

@Service
public class TokenImpl {

    @Autowired
    TokenRepo repo;
    
    public Token crateToken(Token token) {
        Token save=null;
        try {
           save = repo.save(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return save;
    }

    public Token getTokenByTokenString(String token) {
        Token t=null;
        try {
            t =repo.findByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public void deleteToken(Token t) {
        try {
            repo.delete(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
