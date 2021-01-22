package com.yschoi.cluster.controller;

import com.yschoi.cluster.model.ClusterGroup;
import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.cluster.service.ClusterService;
import com.yschoi.login.model.TokenAndId;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/clusters/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId) {
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());

        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }

    @GetMapping("/api/clusters")
    public String getClusters() throws JSONException {
        System.out.println("/pages/cluster");
        if(tokenAndId.getBearerToken() == null || tokenAndId.getBearerToken().equals(""))
            return "";

        return clusterService.getClusters(tokenAndId.getBearerToken());
//        System.out.println(result);

    }

    @GetMapping(value="/api/clusters/delete/{id}")
    public String deleteCluster( @PathVariable(value="id")String id){
//        System.out.println(id);
        String result = clusterService.deleteCluster(id, tokenAndId.getBearerToken());
//        System.out.println(result);

        return result;
    }

    @GetMapping(value="/api/clusters/create/{name}")
    public String createCluster(@PathVariable(value="name")String name){
//        System.out.println(name);
        String result = clusterService.createCluster(name, tokenAndId.getBearerToken());
//        System.out.println(result);

        return result;
    }

    @GetMapping(value="/api/clusters/generateToken/{name}")
    public String generateToken(@PathVariable(value="name")String name){
        System.out.println("generateToken() >>>>> ");
        String result = clusterService.generateToken(name, tokenAndId.getBearerToken());
        System.out.println(result);

        return result;
    }

    @GetMapping(value="/api/clusters/nodes/{nodeId}")
    public String getNode(@PathVariable(value="nodeId")String nodeId){
        System.out.println("getNode() >>>>> ");
        String result = clusterService.getNode(nodeId, tokenAndId.getBearerToken());
        System.out.println(result);

        return result;
    }



    @PostMapping("/api/clusters/modifyClusterName")
    public String modifyClusterName(@RequestBody ClusterVo clusterVo){
        System.out.println("modifyClusterName >>>>>>> ");
        return clusterService.modifyClusterName(clusterVo, tokenAndId.getBearerToken());
    }

}
