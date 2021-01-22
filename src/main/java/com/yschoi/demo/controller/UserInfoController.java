package com.yschoi.demo.controller;

import com.yschoi.demo.model.UserVo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserInfoController {

    @Autowired
    private EmpInfoService empInfoService;

    @GetMapping("/api/projects")
    public void getProjects() {
        System.out.println("/testApi");
        JSONArray result = empInfoService.getProjects();
//        System.out.println(result);

        try {
            for (int i = 0; i < result.length(); i++) {
                System.out.println(result.getJSONObject(i).toString());
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

//    @GetMapping("/api/clusters")
//    public String getClusters() throws JSONException {
//        System.out.println("/pages/cluster");
//        return empInfoService.getClusters();
////        System.out.println(result);
//
//    }

//    @GetMapping("/api/users")
//    public String getUsers() throws JSONException {
//        System.out.println("/pages/users");
//        JSONArray result = empInfoService.getUsers();
////        System.out.println(result);
//
//        String returnStr = "[";
//        try {
//            for (int i = 0; i < result.length(); i++) {
//                if(i==0) returnStr += result.getJSONObject(i).toString();
//                else returnStr += "," + result.getJSONObject(i).toString();
//            }
//            returnStr += "]";
//
//        }catch(JSONException e){
//            e.printStackTrace();
//
//        }
//
//        return returnStr;
//    }


//    @GetMapping(value="/api/clusters/delete/{id}")
//    public String deleteCluster( @PathVariable(value="id")String id){
////        System.out.println(id);
//        String result = empInfoService.deleteCluster(id);
////        System.out.println(result);
//
//        return result;
//    }
//
//    @GetMapping(value="/api/clusters/create/{name}")
//    public String createCluster(@PathVariable(value="name")String name){
////        System.out.println(name);
//        String result = empInfoService.createCluster(name);
////        System.out.println(result);
//
//        return result;
//    }

//    @GetMapping(value="/api/users/delete/{id}")
//    public String deleteUser( @PathVariable(value="id")String id){
////        System.out.println(id);
//        String result = empInfoService.deleteUser(id);
////        System.out.println(result);
//
//        return result;
//    }
//
//    @PostMapping(value="/api/users/create/user")
//    public String createUser(@RequestBody UserVo data){
//
////        System.out.println(data);
//        String result = empInfoService.createUser(data.getName(),data.getPassword(),data.getUsername());
//        System.out.println(result);
//
//        return result;
//    }
//
//    @PostMapping(value="/api/users/setPassword/{id}")
//    public String createUser(@PathVariable(value="id")String id, String password){
//
//        System.out.println(id+", "+password);
////        String result = empInfoService.setUserPassword(name);
////        System.out.println(result);
//
//        return "";
//    }

//    @GetMapping(value="/api/nodes/{id}")
//    public String setClusterNode( @PathVariable(value="id")String id){
//        System.out.println(id);
//        String result = empInfoService.setClusterNode();
////        System.out.println(result);
//
//        return result;
//    }
}
