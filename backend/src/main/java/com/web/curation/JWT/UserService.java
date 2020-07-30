package com.web.curation.JWT;

import com.web.curation.model.user.User;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User signin(String email, String password) {
        if(email.equals("asdf") && password.equals("anObject")){
            return new User();
        } else
        throw new RuntimeException("cant find the user");
    }

    public String getServerInfo(){
        return "info";
    }
}