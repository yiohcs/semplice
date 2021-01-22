package com.yschoi.user.controller;

import com.yschoi.demo.controller.EmpInfoService;
import com.yschoi.demo.model.UserVo;
import com.yschoi.login.model.TokenAndId;
import com.yschoi.user.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/users/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId){
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());

        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }

    @GetMapping("/api/users")
    public String getUsers() throws JSONException {
        System.out.println("/pages/users");
        JSONArray result = userService.getUsers(tokenAndId.getBearerToken());
//        System.out.println(result);

        String returnStr = "[";
        try {
            for (int i = 0; i < result.length(); i++) {
                if(i==0) returnStr += result.getJSONObject(i).toString();
                else returnStr += "," + result.getJSONObject(i).toString();
            }
            returnStr += "]";

        }catch(JSONException e){
            e.printStackTrace();

        }

        return returnStr;
    }

    @GetMapping(value="/api/users/delete/{id}")
    public String deleteUser( @PathVariable(value="id")String id){
//        System.out.println(id);
        String result = userService.deleteUser(id, tokenAndId.getBearerToken());
//        System.out.println(result);

        return result;
    }

    @PostMapping(value="/api/users/create/user")
    public String createUser(@RequestBody UserVo data){

//        System.out.println(data);
        String result = userService.createUser(data.getName(),data.getPassword(),data.getUsername(),tokenAndId.getBearerToken());
        System.out.println(result);

        return result;
    }

    @PostMapping(value="/api/users/setPassword")
    public String setPassword(@RequestBody UserVo userVo){
        System.out.println("setPassword >>>>>  ");
        return userService.setUserPassword(userVo, tokenAndId.getBearerToken());
    }

    @PostMapping(value="/api/users/modifyInfo")
    public String modifyInfo(@RequestBody UserVo userVo){
        System.out.println("modifyInfo >>>>>  ");
        return userService.modifyInfo(userVo, tokenAndId.getBearerToken());
    }

    @GetMapping(value="/api/users/setGlobalRoleBindings/{id}")
    public String setGlobalRoleBindings(@PathVariable(value="id")String id){
//        System.out.println(id);
        String result = userService.setUserDefaultRoleBindings(id, tokenAndId.getBearerToken());
//        System.out.println(result);

        return result;
    }

    @PostMapping(value="/api/users/loginUser")
    public String loginUser(@RequestBody UserVo user){
        System.out.println("loginUser >>> " + user);
        String result = userService.loginUser(user.getUsername(), user.getPassword());
//        System.out.println(result);

        return result;
    }

}
