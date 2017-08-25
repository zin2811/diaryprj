package com.diary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.diary.dao.IDaoUser;
import com.diary.model.ModelUser;

@Service("serviceuser")
public class ServiceUser implements IServiceUser {
    // SLF4J Logging
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("daoUser")
    IDaoUser dao;
    @Override
    public int insertUser(ModelUser user) {
        int result = -1;
        try{
            result = dao.insertUser(user);
        }catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
    
    @Override
    public int userEmailCheck(ModelUser user) {
        int result = -1;
        try{
            result = dao.userEmailCheck(user);
        }catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
    
    @Override
    public int userLogin(ModelUser user) {
        int result = -1;
        try{
            result = dao.userLogin(user);
        }catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
}
