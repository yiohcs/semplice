package com.yschoi.project.controller;

import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.login.model.TokenAndId;
import com.yschoi.project.model.NamespaceVo;
import com.yschoi.project.model.ProjectVo;
import com.yschoi.project.service.ProjectService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    private TokenAndId tokenAndId = new TokenAndId();

    @PostMapping("/api/projects/setTokenAndId")
    public void setBearerToken(@RequestBody TokenAndId tokenAndId) {
        this.tokenAndId.setBearerToken(tokenAndId.getBearerToken());
        this.tokenAndId.setUserId(tokenAndId.getUserId());
        this.tokenAndId.setName(tokenAndId.getName());

        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getBearerToken());
        System.out.println("setBearerToken >>>>  " + this.tokenAndId.getUserId());
    }

    @GetMapping("/api/projects/{clusterId}")
    public String getProjects(@PathVariable(value="clusterId")String clusterId) throws JSONException {
        System.out.println("/projects >>");
        return projectService.getProjects(clusterId, tokenAndId.getBearerToken());
//        System.out.println(result);

    }

    @GetMapping("/api/projects/namespaces/{clusterId}/{projectId}")
    public String getNamespaces(@PathVariable(value="clusterId")String clusterId,@PathVariable(value="projectId")String projectId) throws JSONException {
        System.out.println("/projects/namespaces >>");
        return projectService.getNameSpaces(clusterId, projectId, tokenAndId.getBearerToken());
//        System.out.println(result);

    }

    @PostMapping("/api/projects/namespaceCreate")
    public String createNamespace(@RequestBody NamespaceVo namespaceVo) {
        System.out.println("namespaceCreate >>> ");
        return projectService.createNamespace(namespaceVo ,tokenAndId.getBearerToken());
    }

    @PostMapping("/api/projects/projectCreate")
    public String createProject(@RequestBody ProjectVo projectVo) {
        System.out.println("projectCreate >>> ");
        return projectService.createProject(projectVo ,tokenAndId.getBearerToken());
    }

    @PostMapping("/api/projects/projectDelete")
    public String deleteProject(@RequestBody ProjectVo projectVo) {
        System.out.println("projectDelete >>> ");
        return projectService.deleteProject(projectVo ,tokenAndId.getBearerToken());
    }

    @PostMapping("/api/projects/namespaceDelete")
    public String deleteNamespace(@RequestBody NamespaceVo namespaceVo){
        System.out.println("namespaceDelete >>>>>>> ");
        return projectService.namespaceDelete(namespaceVo, tokenAndId.getBearerToken());
    }
}
