package com.yschoi.demo.controller;

import com.google.gson.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yschoi.demo.model.EmpInfo;
import com.yschoi.demo.model.EmpInfoGroup;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("empInfoService")
public class EmpInfoService {

    public JSONArray getProjects() {
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

            String result = webClient.get()         // GET method
                    .uri("/projects")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            JSONArray returnArr = new JSONArray();
            try {
                JSONArray dataArr = json.getJSONArray("data");
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject dataObj = dataArr.getJSONObject(i);
                    JSONObject returnObj = new JSONObject();
                    returnObj.put("name", dataObj.get("name"));
                    returnObj.put("created", dataObj.get("created"));
                    returnObj.put("createdTS", dataObj.get("createdTS"));
                    returnObj.put("creatorId", dataObj.get("creatorId"));
                    returnObj.put("description", dataObj.get("description"));
                    returnObj.put("id", dataObj.get("id"));
                    returnArr.put(returnObj);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return returnArr;
        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private String apiGetService(String uri) throws SSLException {
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

        String result = webClient.get()         // GET method
                .uri("/" + uri + "?sort=state")    // baseUrl 이후 uri
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                .retrieve()                 // client message 전송
                .bodyToMono(String.class)  // body type : EmpInfo
                .block();                   // await

        return result;
    }

    public String getClusters() {
        String returnStr = "[";

        try {
            String result = apiGetService("clusters");
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            JSONArray returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("id", dataObj.get("id"));
                returnObj.put("state", dataObj.get("state"));
                returnObj.put("name", dataObj.get("name"));
                returnObj.put("provider", dataObj.get("provider"));
                returnObj.put("nodeCount", dataObj.get("nodeCount"));

                for(int j=0;j<dataObj.getJSONObject("requested").length();j++){
                    returnObj.put("memory",dataObj.getJSONObject("requested").get("memory"));
                    returnObj.put("cpu",dataObj.getJSONObject("requested").get("cpu"));
                }
                returnArr.put(returnObj);
            }

            for (int i = 0; i < result.length(); i++) {
                if(i==0) returnStr += returnArr.getJSONObject(i).toString();
                else returnStr += "," + returnArr.getJSONObject(i).toString();
            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";
        return returnStr;
    }

    public EmpInfoGroup getClusters1() {
        Gson gson = new Gson();
        EmpInfoGroup empInfoList = new EmpInfoGroup();
        try {
            String result = apiGetService("clusters");
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            JsonArray jsonObject = new JsonParser().parse(result).getAsJsonObject().getAsJsonArray("data");
//            for(EmpInfo empInfo2: gson.fromJson(jsonObject.getAsJsonArray("data"))){
//                EmpInfo empInfo = new EmpInfo();
//            }
            empInfoList = gson.fromJson(jsonObject, EmpInfoGroup.class);
            System.out.println(empInfoList);

//            JsonObject jsonObject = new JsonParser().parse(resultData).getAsJsonObject();
//            loadBalancerGroupVo = gson.fromJson(jsonObject, LoadBalancerGroupVo.class);
//
//            List<LoadBalancerVo> loadbalancers = new ArrayList<LoadBalancerVo>();
//            for (LoadBalancerVo loadbalancer : loadBalancerGroupVo.getLoadbalancers()) {
//                if (loadbalancer.getTenantId().equalsIgnoreCase(osClient.getToken().getProject().getId())) {
//                    loadbalancers.add(loadbalancer);
//                }
//            }
//
//            loadBalancerGroupVo.setLoadbalancers(loadbalancers);
//
//
//            for (LoadBalancerVo loadbalancer : loadBalancerGroupVo.getLoadbalancers()) {
//                for (ListenerVo listener : loadbalancer.getListeners()) {
//                    ListenerV2 listenerV2 = osClient.octavia().listenerV2().get(listener.getId());
//                    listener.setName(listenerV2.getName());
//                    listener.setProtocol(listenerV2.getProtocol().toString());
//                    listener.setProtocolPort(listenerV2.getProtocolPort());
//                }
//            }
            

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }
        return empInfoList;
    }

    public JSONArray getUsers() {
        try {
            String result = apiGetService("users");
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            JSONArray returnArr = new JSONArray();
            try {
                JSONArray dataArr = json.getJSONArray("data");
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject dataObj = dataArr.getJSONObject(i);
                    JSONObject returnObj = new JSONObject();
                    returnObj.put("state", dataObj.get("state"));
                    returnObj.put("name", dataObj.get("name"));
                    returnObj.put("id", dataObj.get("id"));
                    returnObj.put("username", dataObj.get("username"));

                    returnArr.put(returnObj);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return returnArr;
        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }


    public String deleteCluster(String id) {
//            HTTP/1.1 DELETE /v3/clusters/c-8npvj
//            Host: 89.98.90.174
//            Accept: application/json

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

            String result = webClient.delete()         // POST method
                    .uri("/clusters/" + id)    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String createCluster(String name) {
//        HTTP/1.1 POST /v3/clusters
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 533
//
//        {
//            "amazonElasticContainerServiceConfig": null,
//                "answers": null,
//                "azureKubernetesServiceConfig": null,
//                "clusterTemplateRevisionId": "",
//                "defaultClusterRoleForProjectMembers": "",
//                "defaultPodSecurityPolicyTemplateId": "",
//                "dockerRootDir": "/var/lib/docker",
//                "eksConfig": null,
//                "enableClusterAlerting": false,
//                "enableClusterMonitoring": false,
//                "googleKubernetesEngineConfig": null,
//                "k3sConfig": null,
//                "localClusterAuthEndpoint": null,
//                "name": "test",
//                "rancherKubernetesEngineConfig": null,
//                "rke2Config": null,
//                "scheduledClusterScan": null,
//                "windowsPreferedCluster": false
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

            String result = webClient.post()         // POST method
                    .uri("/clusters")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(533)
                    .bodyValue("{\n" +
                            "\"amazonElasticContainerServiceConfig\": null,\n" +
                            "\"answers\": null,\n" +
                            "\"azureKubernetesServiceConfig\": null,\n" +
                            "\"clusterTemplateRevisionId\": \"\",\n" +
                            "\"defaultClusterRoleForProjectMembers\": \"\",\n" +
                            "\"defaultPodSecurityPolicyTemplateId\": \"\",\n" +
                            "\"dockerRootDir\": \"/var/lib/docker\",\n" +
                            "\"eksConfig\": null,\n" +
                            "\"enableClusterAlerting\": false,\n" +
                            "\"enableClusterMonitoring\": false,\n" +
                            "\"googleKubernetesEngineConfig\": null,\n" +
                            "\"k3sConfig\": null,\n" +
                            "\"localClusterAuthEndpoint\": null,\n" +
                            "\"name\": \"" + name + "\",\n" +
                            "\"rancherKubernetesEngineConfig\": null,\n" +
                            "\"rke2Config\": null,\n" +
                            "\"scheduledClusterScan\": null,\n" +
                            "\"windowsPreferedCluster\": false\n" +
                            "}")
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String deleteUser(String id) {
//        HTTP/1.1 DELETE /v3/users/u-4c99h
//        Host: 89.98.90.174
//        Accept: application/json

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

            String result = webClient.delete()         // POST method
                    .uri("/users/" + id)    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String createUser(String name, String password, String username) {
//        HTTP/1.1 POST /v3/users
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 122
//
//        {
//        "enabled": true,
//        "mustChangePassword": true,
//        "name": "testUser",
//        "password": "okestro2018",
//        "principalIds": [ ],
//        "username": "test1"
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

            String result = webClient.post()         // POST method
                    .uri("/users")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(122)
                    .bodyValue("{\n" +
                            "    \"enabled\": true,\n" +
                            "    \"mustChangePassword\": true,\n" +
                            "    \"name\": \""+name+"\",\n" +
                            "    \"password\": \""+password+"\",\n" +
                            "    \"principalIds\": [ ],\n" +
                            "    \"username\": \""+username+"\"\n" +
                            "}")
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String setUserPassword(String id, String password) {
//    HTTP/1.1 POST /v3/users/user-59hfz?action=setpassword
//    Host: 89.98.90.174
//    Accept: application/json
//    Content-Type: application/json
//    Content-Length: 29
//
//    {
//        "newPassword": "okestro2018"
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

            String result = webClient.post()         // POST method
                    .uri("/users/" + id + "?action=setpassword")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(29)
                    .bodyValue("{\n" +
                            "        \"newPassword\": \""+password+"\"\n" +
                            "        }")
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }


    public String setClusterNode() {
//    HTTP/1.1 POST /v3/nodes
//    Host: 89.98.90.174
//    Accept: application/json
//    Content-Type: application/json
//    Content-Length: 299
//
//    {
//        "amazonec2Config": null,
//            "azureConfig": null,
//            "clusterId": "c-5gght",
//            "controlPlane": true,
//            "customConfig": null,
//            "digitaloceanConfig": null,
//            "etcd": true,
//            "imported": true,
//            "linodeConfig": null,
//            "name": "test",
//            "namespaceId": "",
//            "nodeTemplateId": "",
//            "requestedHostname": "test1",
//            "vmwarevsphereConfig": null,
//            "worker": true
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

            String result = webClient.post()         // POST method
                    .uri("/nodes")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(299)
                    .bodyValue("{\n" +
                            "\"amazonec2Config\": null,\n" +
                            "\"azureConfig\": null,\n" +
                            "\"clusterId\": \"c-5gght\",\n" +
                            "\"controlPlane\": true,\n" +
                            "\"customConfig\": null,\n" +
                            "\"digitaloceanConfig\": null,\n" +
                            "\"etcd\": true,\n" +
                            "\"imported\": true,\n" +
                            "\"linodeConfig\": null,\n" +
                            "\"name\": \"test\",\n" +
                            "\"namespaceId\": \"\",\n" +
                            "\"nodeTemplateId\": \"\",\n" +
                            "\"requestedHostname\": \"test1\",\n" +
                            "\"vmwarevsphereConfig\": null,\n" +
                            "\"worker\": true\n" +
                            "}")
                    .header("Authorization", "Bearer token-48p8v:cmd6fbcktkbzcbd8zwmdt52mr24bxf8fcszqkknl5lm6tsfcwkztcf")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

}
