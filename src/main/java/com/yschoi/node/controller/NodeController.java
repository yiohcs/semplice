package com.yschoi.node.controller;

import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.login.model.TokenAndId;
import com.yschoi.node.model.NodeVo;
import com.yschoi.node.service.NodeService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NodeController {

    @Autowired
    NodeService nodeService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/nodes/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId) {
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());

        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }

    @GetMapping("/api/nodes/{clusterId}")
    public String getNodes(@PathVariable(value="clusterId")String clusterId) {
        System.out.println("/nodes >>");
        return nodeService.getNodes(clusterId, tokenAndId.getBearerToken());
//        System.out.println(result);

    }

    @PostMapping("/api/nodes/modifyNodeName")
    public String modifyClusterName(@RequestBody NodeVo nodeVo){
        System.out.println("modifyClusterName >>>>>>> ");
        return nodeService.modifyNodeName(nodeVo, tokenAndId.getBearerToken());
    }

    @PostMapping(value="/api/nodes/getClusterToken")
    public String getClusterToken(@RequestBody NodeVo nodeVo){
        System.out.println("getClusterToken() >>>>> ");
        String result = nodeService.getClusterToken(nodeVo , tokenAndId.getBearerToken());
        System.out.println(result);

        return result;
    }

    @PostMapping(value="/api/nodes/delete")
    public String deleteNode(@RequestBody NodeVo nodeVo){
        System.out.println("deleteNode() >>>>> ");
        String result = nodeService.deleteNode(nodeVo , tokenAndId.getBearerToken());
        System.out.println(result);

        return result;
    }
}
