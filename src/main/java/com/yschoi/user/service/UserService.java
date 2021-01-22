package com.yschoi.user.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yschoi.demo.model.EmpInfoGroup;
import com.yschoi.demo.model.UserVo;
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

@Service("userService")
public class UserService {

    private String apiGetService(String uri, String bearerToken) throws SSLException {
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
        System.out.println("Users Token>>>>> " + bearerToken);
        WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                .build();
        // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);

        String result = webClient.get()         // GET method
                .uri("/" + uri + "?sort=state")    // baseUrl 이후 uri
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()                 // client message 전송
                .bodyToMono(String.class)  // body type : EmpInfo
                .block();                   // await

        return result;
    }

    public JSONArray getUsers(String bearerToken) {
        JSONArray returnArr = null;
        try {
            String result = apiGetService("users", bearerToken);
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = new JSONArray();
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

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }
        return returnArr;
    }

    public String deleteUser(String id, String bearerToken) {
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
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String createUser(String name, String password, String username, String bearerToken) {
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
//                    .contentLength(122)
                    .bodyValue("{\n" +
                            "    \"enabled\": true,\n" +
                            "    \"mustChangePassword\": true,\n" +
                            "    \"name\": \""+name+"\",\n" +
                            "    \"password\": \""+password+"\",\n" +
                            "    \"principalIds\": [ ],\n" +
                            "    \"username\": \""+username+"\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String setUserPassword(UserVo userVo, String bearerToken) {
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
                    .uri("/users/" + userVo.getId() + "?action=setpassword")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(29)
                    .bodyValue("{\n" +
                            "\"newPassword\": \""+userVo.getPassword()+"\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println("setPassword >>>>>>> "+result);
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }


    public String setClusterNode(String bearerToken) {
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
//                    .contentLength(299)
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
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String setUserDefaultRoleBindings(String id, String bearerToken) {
//    HTTP/1.1 POST /v3/globalrolebindings
//    Host: 89.98.90.174
//    Accept: application/json
//    Content-Type: application/json
//    Content-Length: 52
//
//    {
//        "globalRoleId": "user",
//            "name": "",
//            "userId": "u-fbkw9"
//    }
        System.out.println("Usermethod : setUserGlobalRoleBindings >>>>>>");

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
                    .uri("/globalRoleBindings?userId=" + id)    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(52)
                    .bodyValue("{\n" +
                            "\"globalRoleId\": \"user\",\n" +
                            "\"name\": \"\",\n" +
                            "\"userId\": \"" + id + "\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String modifyInfo(UserVo userVo, String bearerToken) {
//        HTTP/1.1 PUT /v3/users/u-2b95z
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 457
//
//        {
//            "annotations": {
//            "lifecycle.cattle.io/create.mgmt-auth-users-controller": "true"
//        },
//            "conditions": [ ],
//            "created": "2020-12-12T16:14:24Z",
//                "creatorId": "user-59hfz",
//                "enabled": null,
//                "id": "u-2b95z",
//                "labels": {
//            "cattle.io/creator": "norman"
//        },
//            "mustChangePassword": false,
//                "name": "testUser",
//                "ownerReferences": [ ],
//            "principalIds": [
//            "local://u-2b95z"
//],
//            "state": "active",
//                "transitioning": "no",
//                "transitioningMessage": "",
//                "username": "testUser",
//                "uuid": "0d86fbc7-c889-46a6-baf4-60883f96703e"
//        }
        System.out.println("Usermethod : modifyInfo >>>>>>");
        System.out.println(userVo.getName());
        System.out.println(userVo.getUsername());

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

            String result = webClient.put()         // POST method
                    .uri("/users/" + userVo.getId())    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(52)
                    .bodyValue("{\n" +
                            "\"name\":\""+userVo.getName()+"\",\n" +
                            "\"username\":\""+userVo.getUsername()+"\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            System.out.println("modifyInfo >>>>>>> "+result);
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String loginUser(String id, String password) {
///   HTTP/1.1 POST /v3-public/localProviders/local?action=login
//    Host: 89.98.90.174
//    Accept: application/json
//    Content-Type: application/json
//    Content-Length: 103
//
//    {
//        "description": "UI Session",
//            "password": "okestro2018",
//            "responseType": null,
//            "ttl": 0,
//            "username": "testUser"
//    }

        System.out.println("Usermethod : loginUser >>>>>>");

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
                    .uri("-public/localProviders/local?action=login")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(52)
                    .bodyValue("{\n" +
                            "\"description\": \"LoginSession\",\n" +
                            "\"password\": \""+password+"\",\n" +
                            "\"responseType\": null,\n" +
                            "\"ttl\": 60000,\n" +
                            "\"username\": \""+id+"\"\n" +
                            "}")
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            return result;
        }catch(Exception e){
            System.out.println("login failed");
//            e.printStackTrace();
        }
        return null;
    }


}
