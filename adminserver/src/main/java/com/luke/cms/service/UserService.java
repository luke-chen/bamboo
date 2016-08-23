package com.luke.cms.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luke.cms.model.rspnstatus.Failed;
import com.luke.cms.model.rspnstatus.Result;
import com.luke.cms.model.rspnstatus.Success;
import com.luke.cms.model.user.Authority;
import com.luke.cms.model.user.User;
import com.luke.cms.model.user.UserInfo;
import com.luke.cms.persistence.mapper.UserMapper;

@Service
public class UserService {
	
	private Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();
	
    public static HashMap<String, Integer> ROLE_AUTH = new HashMap<String, Integer>();
    static {
        // admin authority
        ROLE_AUTH.put("ROLE_ADMIN", 0);
        // user authority
        ROLE_AUTH.put("ROLE_USER", 1);
    }
    
    @Autowired
    private UserMapper userMapper;

    public UserInfo getUser(String username) {
        return userMapper.getUser(username);
    }

    public List<UserInfo> getAllUsers() {
        return userMapper.getAll();
    }
    
    public List<UserInfo> getUsersByRoleUser() {
        return userMapper.getUsersByRoleUser();
    }

    public void addUser(User user) {
        userMapper.addUser(user);
    }
    
    public int updatePasswordByName(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Encoder.encodePassword(password, null));
        return userMapper.updatePasswordByName(user);
    }

    public int removeUser(String username) {
        return userMapper.removeUser(username);
    }
    
    public void addAuthority(Authority authority) {
        userMapper.addAuth(authority);
    }
    
    public int removeAuthority(String username) {
        return userMapper.removeAuthority(username);
    }
    
    public Result addUserAndAuthority(String username, String password, String authority) {
        if(!checkUserInfo(username, password))
        	return new Failed("用户名或密码不合法,密码为6-12位");
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Encoder.encodePassword(password, null));
        addUser(user);
        Authority auth = new Authority();
        auth.setUsername(user.getUsername());
        auth.setAuthority(authority);
        if(!ROLE_AUTH.containsKey(authority))
            return new Failed("该权限不存在");
        addAuthority(auth);
        return new Success();
    }
    
    public Result deleteUserAndAuthority(String username) {
        return removeAuthority(username) > 0 && removeUser(username) > 0 ? new Success()
                : new Failed("该用户不存在");
    }
    
    public Result updateUserAndAuthorityByName(String username, String password, String authority) {
    	if(!ROLE_AUTH.containsKey(authority))
            return new Failed("该权限不存在");
    	if(updatePasswordByName(username, password) <= 0)
            return new Failed("该用户不存在");
    	User user = new User();
		user.setUsername(username);
		user.setPassword(md5Encoder.encodePassword(password, null));;
        removeAuthority(user.getUsername());
        Authority auth = new Authority();
        auth.setUsername(user.getUsername());
        auth.setAuthority(authority);
        addAuthority(auth);
        return new Success();
    }
    
    /**
     * Check that user's name and password are valid
     * @param username
     * @param password
     * @return True if it is valid, else False.
     */
    private boolean checkUserInfo(String username, String password) {
		return !(username == null || username.isEmpty()
				|| password == null || password.isEmpty() || password.length() < 6 && password.length() > 12);
    }
}
