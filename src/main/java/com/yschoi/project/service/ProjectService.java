package com.yschoi.project.service;

import com.yschoi.cluster.model.ClusterGroup;
import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.project.model.NamespaceVo;
import com.yschoi.project.model.ProjectVo;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Service("projectService")
public class ProjectService {

    public String getProjects(String clusterId, String bearerToken) {
        String returnStr = "[";
        JSONArray returnArr = null;
        System.out.println("clusterId >>>> "+clusterId);

        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
            String result = webClient.get()         // GET method
                    .uri("/clusters/"+clusterId+"/projects")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            String test = json.get("data").toString();
            System.out.println(test);

            returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("id", dataObj.get("id"));
                returnObj.put("state", dataObj.get("state"));
                returnObj.put("name", dataObj.get("name"));
                returnObj.put("clusterId", dataObj.get("clusterId"));

                returnArr.put(returnObj);
            }
            System.out.println("length: " + json.getJSONArray("data").length());
            System.out.println(returnArr);
            for (int i = 0; i < returnArr.length(); i++) {
                if(i==0) returnStr += returnArr.getJSONObject(i).toString();
                else returnStr += "," + returnArr.getJSONObject(i).toString();
            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";



        return returnStr;
    }

    public String getNameSpaces(String clusterId, String projectId, String bearerToken) {
        String returnStr = "[";
        JSONArray returnArr = null;

        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
            String result = webClient.get()         // GET method
                    .uri("/clusters/"+clusterId+"/namespaces")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("state", dataObj.get("state"));
                returnObj.put("projectId", dataObj.get("projectId"));
                returnObj.put("id", dataObj.get("id"));
                returnObj.put("name", dataObj.get("name"));
                returnObj.put("created", dataObj.get("created"));

                returnArr.put(returnObj);
            }

            boolean check = false;
            System.out.println(returnArr);
            for(int i=0;i<returnArr.length();i++){
                if(returnArr.getJSONObject(i).get("projectId").toString().equals(projectId)){
                    if(!check){returnStr += returnArr.getJSONObject(i).toString(); check=true;}
                    else returnStr += "," + returnArr.getJSONObject(i).toString();
                }
            }

//            for (int i = 0; i < returnArr.length(); i++) {
//                if(i==0) returnStr += returnArr.getJSONObject(i).toString();
//                else returnStr += "," + returnArr.getJSONObject(i).toString();
//            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";



        return returnStr;
    }

    public String createNamespace(NamespaceVo namespaceVo, String bearerToken) {
        System.out.println(namespaceVo.getName());
        System.out.println(namespaceVo.getProjectId());
        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
            String result = webClient.post()         // POST method
                    .uri("/clusters/"+namespaceVo.getClusterId()+"/namespaces")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\n" +
                            "\"containerDefaultResourceLimit\": null,\n" +
                            "\"name\": \""+namespaceVo.getName()+"\",\n" +
                            "\"projectId\": \""+namespaceVo.getProjectId()+"\",\n" +
                            "\"resourceQuota\": null\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println(result);

            return result;
        }catch (SSLException e){
            e.printStackTrace();
        }

        return null;
    }

    public String createProject(ProjectVo projectVo, String bearerToken) {

//        HTTP/1.1 POST /v3/projects
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 179
//
//        {
//            "clusterId": "c-wkmlk",
//                "containerDefaultResourceLimit": null,
//                "enableProjectMonitoring": true,
//                "name": "asd",
//                "namespaceDefaultResourceQuota": null,
//                "namespaceId": "",
//                "resourceQuota": null
//        }

        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
            String result = webClient.post()         // POST method
                    .uri("/clusters/"+projectVo.getClusterId()+"/projects")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\n" +
                            "\"clusterId\": \""+projectVo.getClusterId()+"\",\n" +
                            "\"containerDefaultResourceLimit\": null,\n" +
                            "\"enableProjectMonitoring\": true,\n" +
                            "\"name\": \""+projectVo.getName()+"\",\n" +
                            "\"namespaceDefaultResourceQuota\": null,\n" +
                            "\"namespaceId\": \"\",\n" +
                            "\"resourceQuota\": null\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            Thread.sleep(1000);

            System.out.println(result);

            return result;
        }catch (SSLException | InterruptedException e){
            e.printStackTrace();
        }

        return null;
    }




    public String deleteProject(ProjectVo projectVo, String bearerToken) {

//    HTTP/1.1 DELETE /v3/projects/c-wkmlk:p-q74l6
//    Host: 89.98.90.174
//    Accept: application/json

        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
            String result = webClient.delete()         // POST method
                    .uri("/projects/" +projectVo.getProjectId())    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println(result);


            while(true) {
                Thread.sleep(1000);
                String result1 = webClient.get()         // GET method
                        .uri("/clusters/" + projectVo.getClusterId() + "/projects")    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await


                JSONObject json = null;
                json = new JSONObject(result1);
                boolean check = false;
                System.out.println(json.get("data").toString());

                for (int i = 0; i < json.getJSONArray("data").length(); i++) {
                    System.out.println(">>>>>>>>>>>"+json.getJSONArray("data").getJSONObject(i).toString());
                    if(!json.getJSONArray("data").getJSONObject(i).get("clusterId").toString().equals(projectVo.getProjectId())){
                        check = true;
                        if(i < json.getJSONArray("data").length()-1){
                            check = false;
                        }
                    }else{
                        break;
                    }
                }
                System.out.println("check >>>>>>>> " + check);
                if(check)
                    break;
            }

            return result;
        }catch (SSLException | JSONException | InterruptedException e){
            e.printStackTrace();
        }

        return null;
    }

    public String namespaceDelete(NamespaceVo namespaceVo, String bearerToken) {
//        HTTP/1.1 PUT /v3/clusters/c-wkmlk
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//
//    {
//        "name": "yschoi"
//    }

        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 7000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(7000, TimeUnit.MILLISECONDS))
                                            .addHandlerLast(new WriteTimeoutHandler(7000, TimeUnit.MILLISECONDS))))
                    .secure(spec ->
                            spec.sslContext(sslContext));
            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

            WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                    .build();
            // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
                Thread.sleep(1000);
               String result = webClient.delete()         // POST method
                        .uri("/clusters/" + namespaceVo.getClusterId() + "/namespaces/" + namespaceVo.getName())    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await


            while(true) {
                Thread.sleep(1000);
                String result1 = webClient.get()         // GET method
                        .uri("/clusters/" + namespaceVo.getClusterId() + "/namespaces")    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await


                JSONObject json = null;
                json = new JSONObject(result1);
                boolean check = false;
                System.out.println(json.getJSONArray("data").toString());

                for (int i = 0; i < json.getJSONArray("data").length(); i++) {
                    System.out.println(">>>>>>>>>>>"+json.getJSONArray("data").getJSONObject(i).toString());
                    if(!json.getJSONArray("data").getJSONObject(i).get("name").toString().equals(namespaceVo.getName())){
                        check = true;
                        if(i < json.getJSONArray("data").length()-1){
                            check = false;
                        }
                    }else{
                        break;
                    }
                }
                System.out.println("check >>>>>>>> " + check);
                if(check)
                    break;
            }


            return result;
        }catch(SSLException | InterruptedException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
