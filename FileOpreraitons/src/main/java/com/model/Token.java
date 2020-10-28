package com.model;


import java.sql.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   
    private String token;
    
    @OneToOne
    private UserEntity entity;
    
    
//    @Temporal(TemporalType.TIMESTAMP) 
    private Date doc;
    
    
    public Token() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Token(UserEntity entity) {
        super();
        this.entity = entity;
        this.doc = new Date(new java.util.Date().getTime());
        this.token = UUID.randomUUID().toString();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserEntity getEntity() {
        return entity;
    }
    public void setEntity(UserEntity entity) {
        this.entity = entity;
    }
    /*
     * public Date getDoc() { return doc; } public void setDoc(Date doc) { this.doc
     * = doc; }
     */
    
    
    
    
}
