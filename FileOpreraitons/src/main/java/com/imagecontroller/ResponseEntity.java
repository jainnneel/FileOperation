package com.imagecontroller;

import java.util.List;

public class ResponseEntity {

    private Object status;
    private String link;
    public ResponseEntity() {
        super();
        // TODO Auto-generated constructor stub
    }
    public ResponseEntity(Object status, String link) {
        super();
        this.status = status;
        this.link = link;
    }
    public Object getStatus() {
        return status;
    }
    public void setStatus(Object status) {
        this.status = status;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    
    
  
    
}
