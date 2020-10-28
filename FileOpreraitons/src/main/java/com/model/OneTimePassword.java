package com.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class OneTimePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String otp;
    private int count;

//    @Temporal(TemporalType.TIMESTAMP)
    private Date doc;

    @OneToOne
    private UserEntity entity;

    public OneTimePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OneTimePassword(UserEntity entity) {
        super();
        this.otp = (int) (Math.random() * 1000000 + 100001) + "";
        System.out.println((int) Math.random() * 1000000);
        this.doc = new Date(new java.util.Date().getTime());
        this.entity = entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public UserEntity getEntity() {
        return entity;
    }

    public void setEntity(UserEntity entity) {
        this.entity = entity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
}
