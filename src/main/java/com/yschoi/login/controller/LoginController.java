package com.yschoi.login.controller;

import com.yschoi.login.model.TokenAndId;
import com.yschoi.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/login/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId) {
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }
    @GetMapping("/api/login/getBearerToken")
    public String getBearerToken(){
        return tokenAndId.getBearerToken();
    }

    @GetMapping("/api/login/getToken")
    public String getToken(){
        System.out.println("getToken >>>>> ");

        String result = loginService.getToken(tokenAndId.getBearerToken());

        return result;
    }


    @GetMapping("/api/login/removeToken/{name}")
    public String removeToken(@PathVariable(value="name")String name){
        System.out.println("removeToken >>>> ");

        String result = loginService.removeToken(name ,tokenAndId.getBearerToken());

        return result;
    }
}
