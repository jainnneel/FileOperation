package com.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String pass;
    private Date d;
    private String mobile;
    private String role = "USER";
    private boolean isEnable;
    private Date freeTierExpire;
    public RegUser() {
        super();
        // TODO Auto-generated constructor stub
    }
    public RegUser(int id, String name, String email, String pass, Date d, String mobile, String role, boolean isEnable,
            Date freeTierExpire) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.d = d;
        this.mobile = mobile;
        this.role = role;
        this.isEnable = isEnable;
        this.freeTierExpire = freeTierExpire;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public Date getD() {
        return d;
    }
    public void setD(Date d) {
        this.d = d;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isEnable() {
        return isEnable;
    }
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
    public Date getFreeTierExpire() {
        return freeTierExpire;
    }
    public void setFreeTierExpire(Date freeTierExpire) {
        this.freeTierExpire = freeTierExpire;
    }
    
    
}
