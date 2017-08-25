package com.diary;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diary.model.ModelUser;
import com.diary.service.IServiceUser;
/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
    @Qualifier("serviceuser")
    IServiceUser svr;
	
	@RequestMapping(value = "/rest/login", method = {RequestMethod.GET, RequestMethod.POST} )
    @ResponseBody
    public int login(Model model
            , @RequestParam(value="id", defaultValue="") String id 
            , @RequestParam(value="pw", defaultValue="") String pw) {
        logger.info("/login");
        ModelUser user = new ModelUser(id, pw);
        int result = -1;
        
        if(svr == null)
            result = -1;
        else
            result = svr.userLogin(user);
        
        return result;
        
    }   
	@RequestMapping(value = "/rest/idcheck", method = {RequestMethod.GET, RequestMethod.POST} )
    @ResponseBody
    public int idCheck(Model model
            , @RequestParam(value="id", defaultValue="") String id) {
        logger.info("/idcheck");
        ModelUser user = new ModelUser(id, "1");
        int result = -1;
        
        if(svr == null)
            result = -1;
        else
            result = svr.userEmailCheck(user);
        
        return result;
        
    }
	@RequestMapping(value = "/rest/insertone", method = {RequestMethod.GET, RequestMethod.POST} )
    @ResponseBody
    public int insertUser(Model model
            , @RequestParam(value="id", defaultValue="") String id 
            , @RequestParam(value="pw", defaultValue="") String pw) {
        logger.info("/login");
        ModelUser user = new ModelUser(id, pw);
        int result = -1;
        
        if(svr == null)
            result = -1;
        else
            result = svr.insertUser(user);
        
        return result;
    }
    
}
