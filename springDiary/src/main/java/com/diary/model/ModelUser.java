package com.diary.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelUser {
    String email;
    String password;
    public ModelUser(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }
    public ModelUser() {
        super();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "ModelUser [email=" + email + ", password=" + password + "]";
    }
    
}
