package com.testcase;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.diary.model.ModelUser;
import com.diary.service.IServiceUser;

public class testLogin {
    // SLF4J Logging
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    

    private static ApplicationContext context = null;
    private static IServiceUser service = null;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context= new ClassPathXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml");
        service=context.getBean("serviceUser", IServiceUser.class);
    }
    
    @Test
    public void testinsert() {
        String email = "asdas@asdasd.com";
        String password = "asdfadsf";
        ModelUser user = new ModelUser(email,password);
        
        int result = service.insertUser(user);
        assertEquals(result, 1);
        
        
    }
    @Test
    public void testcheck() {
        String email = "asdas@asdasd.com";
        String password = "asdfadsf";
        ModelUser user = new ModelUser(email,password);
        
        int result = service.userEmailCheck(user);
        assertNotNull(result);        
        
    }
    @Test
    public void testlogin() {
        String email = "asdas@asdasd.com";
        String password = "asdfadsf";
        ModelUser user = new ModelUser(email,password);
        
        int result = service.userLogin(user);
        assertNotNull(result);
        
        
    }
}
