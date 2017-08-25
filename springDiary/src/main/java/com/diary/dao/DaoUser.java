package com.diary.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diary.model.ModelUser;
@Repository("daoUser")
public class DaoUser implements IDaoUser {
    // SLF4J Logging
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    @Qualifier("sqlSession")
    private SqlSession session;
    @Override
    public int insertUser(ModelUser user) {
        // TODO Auto-generated method stub
        return session.insert("mapper.mysql.mapperUser.insertUser",user);
    }
    
    @Override
    public int userEmailCheck(ModelUser user) {
        
        return session.selectOne("mapper.mysql.mapperUser.userEmailCheck",user);
    }
    
    @Override
    public int userLogin(ModelUser user) {
        // TODO Auto-generated method stub
        return session.selectOne("mapper.mysql.mapperUser.userLogin",user);
    }
}
