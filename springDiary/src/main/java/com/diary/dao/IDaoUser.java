package com.diary.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diary.model.ModelUser;

public interface IDaoUser {
    int insertUser(ModelUser user);
    
    int userEmailCheck(ModelUser user);
    
    int userLogin(ModelUser user);
    
}
