package com.yschoi.catalog.service;

import com.yschoi.catalog.model.CatalogVo;
import com.yschoi.catalog.model.ServiceVo;
import com.yschoi.cluster.model.ClusterGroup;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service("catalogService")
public class CatalogService {

    public String getServices(CatalogVo catalogVo, String bearerToken) {
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
            String result ="";
            result = webClient.get()         // GET method
                    .uri("/project/" + catalogVo.getProjectId() + "/services")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println("result : " + result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("clusterIp", dataObj.get("clusterIp"));
                returnObj.put("id", dataObj.get("id"));
                returnObj.put("projectId", dataObj.get("projectId"));
                returnObj.put("ports", dataObj.get("ports"));

                returnArr.put(returnObj);
            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";

        System.out.println("getServices >>>>> ");
        System.out.println(returnArr.toString());

        return returnArr.toString();
    }

    public String createCatalog(CatalogVo catalogVo, String bearerToken) {

//        String name,String namespaceId, String projectId, String externalId,
        System.out.println("createCatalog >>>>> ");

        String name = catalogVo.getName();
        String namespaceId = catalogVo.getNamespaceId();
        String projectId = catalogVo.getProjectId();
        String externalId = catalogVo.getExternalId();

//        projectId = "c-wkmlk:p-jbdm9";
//        namespaceId = "yschoi-tt";
//        name = "ys-gra";
//        externalId = "catalog://?catalog=library&template=grafana&version=3.8.6";

        System.out.println(projectId);
        System.out.println(namespaceId);
        System.out.println(name);
        System.out.println(bearerToken);

//        HTTP/1.1 POST /v3/project/c-wkmlk:p-jbdm9/apps
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 229
//
//        {
//            "appRevisionId": "",
//                "externalId": "catalog://?catalog=library&template=grafana&version=3.8.6",
//                "multiClusterAppId": "",
//                "name": "ys-gra",
//                "namespaceId": "yschoi-tt",
//                "projectId": "c-wkmlk:p-jbdm9",
//                "prune": false,
//                "timeout": 300,
//                "wait": false
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

            String result = null;
            JSONObject json = null;
            String returnName = "";

            result = webClient.post()         // POST method
                    .uri("/project/" + projectId + "/apps")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(533)
                    .bodyValue("{\n" +
                            "\"appRevisionId\": \"\",\n" +
                            "\"externalId\": \""+externalId+"\",\n" +
                            "\"multiClusterAppId\": \"\",\n" +
                            "\"name\": \""+name+"\",\n" +
                            "\"namespaceId\": \""+namespaceId+"\",\n" +
                            "\"projectId\": \""+projectId+"\",\n" +
                            "\"prune\": false,\n" +
                            "\"targetNamespace\": \""+namespaceId+"\",\n" +
                            "\"timeout\": 300,\n" +
                            "\"wait\": false\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            int cnt = 0;
            while(true){
            json = new JSONObject(result);


                cnt++;
                Thread.sleep(2000);
                System.out.println(json.get("state"));
                if(json.get("state").toString().equals("active")){
                    returnName = json.get("name").toString();
                    break;
                }
                if(cnt >= 10)
                    break;
            }
            if(returnName.equals(""))
                return null;

            // 서비스 목록 가져오기
            String returnId = "";
            String clusterIp = "";
            String appId = "";
            name = "";
            namespaceId = "";
            // 30000 - 32767
            Random rand = new Random();
            int nodePort = 0;
            cnt = 0;
            ServiceVo serviceVo = new ServiceVo();
            while(true){
            result = webClient.get()         // GET method
                    .uri("/project/" + projectId + "/services")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            json = new JSONObject(result);

                cnt++;
                Thread.sleep(2000);

                for(int i=0; i<json.getJSONArray("data").length();i++){
                    System.out.println(cnt + "   " + i);
                    if(json.getJSONArray("data").getJSONObject(i).getJSONObject("selector").get("release").toString().equals(returnName)){
                        returnId = json.getJSONArray("data").getJSONObject(i).get("id").toString();
                        clusterIp = json.getJSONArray("data").getJSONObject(i).get("clusterIp").toString();
                        appId = returnName;
                        name = json.getJSONArray("data").getJSONObject(i).get("name").toString();
                        namespaceId = json.getJSONArray("data").getJSONObject(i).get("namespaceId").toString();
                        nodePort = rand.nextInt(2768)+30000;
                        System.out.println(returnId);
                        System.out.println(clusterIp);
                        System.out.println(appId);
                        System.out.println(name);
                        System.out.println(namespaceId);
                        System.out.println(nodePort);
                        break;
                    }
                }
                if(!returnId.equals(""))
                    break;
                if(cnt >=20)
                    break;
            }
            if(returnId.equals(""))
                return null;

//            json.getJSONArray("data").getJSONObject(0).getJSONObject("selector").get("release");


            // 서비스 추가
            cnt = 0;
            while(true) {
                System.out.println("\ncreateApps Services >>>>>>>>> " + cnt);
                cnt++;
                Thread.sleep(2000);

                result = webClient.put()         // POST method
                        .uri("/project/" + projectId + "/services/" + returnId)    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(533)
                        .bodyValue("{\n" +
                                "\"annotations\": { },\n" +
                                "\"clusterIp\": \"" + clusterIp + "\",\n" +
                                "\"created\": \"\",\n" +
                                "\"creatorId\": null,\n" +
                                "\"externalIPs\": [ ],\n" +
                                "\"externalTrafficPolicy\": \"Cluster\",\n" +
                                "\"healthCheckNodePort\": 0,\n" +
                                "\"ipAddresses\": [ ],\n" +
                                "\"kind\": \"NodePort\",\n" +
                                "\"labels\": {\n" +
                                "\"app\": \"grafana\",\n" +
                                "\"chart\": \"grafana-3.8.6\",\n" +
                                "\"heritage\": \"Tiller\",\n" +
                                "\"io.cattle.field/appId\": \"" + appId + "\",\n" +
                                "\"release\": \"" + appId + "\"\n" +
                                "},\n" +
                                "\"loadBalancerSourceRanges\": [ ],\n" +
                                "\"name\": \"" + name + "\",\n" +
                                "\"namespaceId\": \"" + namespaceId + "\",\n" +
                                "\"ownerReferences\": [ ],\n" +
                                "\"ports\": [\n" +
                                "{\n" +
                                "\"name\": \"service\",\n" +
                                "\"nodePort\": " + nodePort + ",\n" +
                                "\"port\": 80,\n" +
                                "\"protocol\": \"TCP\",\n" +
                                "\"targetPort\": 3000,\n" +
                                "\"type\": \"/v3/project/schemas/servicePort\"\n" +
                                "}\n" +
                                "],\n" +
                                "\"projectId\": \"" + projectId + "\",\n" +
                                "\"publicEndpoints\": [ ],\n" +
                                "\"publishNotReadyAddresses\": false,\n" +
                                "\"selector\": {\n" +
                                "\"app\": \"grafana\",\n" +
                                "\"release\": \"" + appId + "\"\n" +
                                "},\n" +
                                "\"sessionAffinity\": \"None\",\n" +
                                "\"sessionAffinityConfig\": null,\n" +
                                "\"state\": \"active\",\n" +
                                "\"targetDnsRecordIds\": [ ],\n" +
                                "\"targetWorkloadIds\": [ ],\n" +
                                "\"topologyKeys\": [ ],\n" +
                                "\"transitioning\": \"no\",\n" +
                                "\"transitioningMessage\": \"\",\n" +
                                "\"uuid\": \"\",\n" +
                                "\"workloadId\": null\n" +
                                "}")
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await

                json = new JSONObject(result);

                boolean returnBool = false;

                for(int i=0;i<json.length();i++){
                    if(json.get("state").toString().equals("active")){
                        returnBool = true;
                    }
                    if(returnBool)
                        break;
                }


                if(cnt >= 20 || returnBool)
                    break;
            }

            System.out.println("\ncreateApps >>>>>>> return ");
            return result;
        }catch(SSLException | JSONException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }


    public String addService(ServiceVo serviceVo, String bearerToken) {
        String clusterIp = serviceVo.getClusterIp();
        String appId = serviceVo.getAppId();
        String name = serviceVo.getName();
        String namespaceId = serviceVo.getNamespaceId();
        String projectId = serviceVo.getProjectId();
        int nodePort = serviceVo.getPorts().getNodePort();

        clusterIp = "10.43.143.157";
        appId = "ys-gra";
        name = "ys-gra-grafana";
        namespaceId = "yschoi-tt";
        projectId = "c-wkmlk:p-jbdm9";
        nodePort = 31950;


//        HTTP/1.1 PUT /v3/project/c-wkmlk:p-jbdm9/services/yschoi-tt:ys-gra-grafana
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 941
//
//        {
//            "annotations": { },
//            "clusterIp": "10.43.143.157",
//                "created": "2021-01-04T02:20:12Z",
//                "creatorId": null,
//                "externalIPs": [ ],
//            "externalTrafficPolicy": "Cluster",
//                "healthCheckNodePort": 0,
//                "ipAddresses": [ ],
//            "kind": "NodePort",
//                "labels": {
//            "app": "grafana",
//                    "chart": "grafana-3.8.6",
//                    "heritage": "Tiller",
//                    "io.cattle.field/appId": "ys-gra",
//                    "release": "ys-gra"
//        },
//            "loadBalancerSourceRanges": [ ],
//            "name": "ys-gra-grafana",
//                "namespaceId": "yschoi-tt",
//                "ownerReferences": [ ],
//            "ports": [
//            {
//                "name": "service",
//                    "nodePort": 31949,
//                    "port": 80,
//                    "protocol": "TCP",
//                    "targetPort": 3000,
//                    "type": "/v3/project/schemas/servicePort"
//            }
//],
//            "projectId": "c-wkmlk:p-jbdm9",
//                "publicEndpoints": [ ],
//            "publishNotReadyAddresses": false,
//                "selector": {
//            "app": "grafana",
//                    "release": "ys-gra"
//        },
//            "sessionAffinity": "None",
//                "sessionAffinityConfig": null,
//                "state": "active",
//                "targetDnsRecordIds": [ ],
//            "targetWorkloadIds": [ ],
//            "topologyKeys": [ ],
//            "transitioning": "no",
//                "transitioningMessage": "",
//                "uuid": "bb00a7c8-ecdb-4edd-a580-2e5f82b06b91",
//                "workloadId": null
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

            String result = webClient.put()         // POST method
                    .uri("/project/" + projectId + "/services")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(533)
                    .bodyValue("{\n" +
                            "\"annotations\": { },\n" +
                            "\"clusterIp\": \""+clusterIp+"\",\n" +
                            "\"created\": \"\",\n" +
                            "\"creatorId\": null,\n" +
                            "\"externalIPs\": [ ],\n" +
                            "\"externalTrafficPolicy\": \"Cluster\",\n" +
                            "\"healthCheckNodePort\": 0,\n" +
                            "\"ipAddresses\": [ ],\n" +
                            "\"kind\": \"NodePort\",\n" +
                            "\"labels\": {\n" +
                            "\"app\": \"grafana\",\n" +
                            "\"chart\": \"grafana-3.8.6\",\n" +
                            "\"heritage\": \"Tiller\",\n" +
                            "\"io.cattle.field/appId\": \""+appId+"\",\n" +
                            "\"release\": \""+appId+"\"\n" +
                            "},\n" +
                            "\"loadBalancerSourceRanges\": [ ],\n" +
                            "\"name\": \""+name+"\",\n" +
                            "\"namespaceId\": \""+namespaceId+"\",\n" +
                            "\"ownerReferences\": [ ],\n" +
                            "\"ports\": [\n" +
                            "{\n" +
                            "\"name\": \"service\",\n" +
                            "\"nodePort\": "+nodePort+",\n" +
                            "\"port\": 80,\n" +
                            "\"protocol\": \"TCP\",\n" +
                            "\"targetPort\": 3000,\n" +
                            "\"type\": \"/v3/project/schemas/servicePort\"\n" +
                            "}\n" +
                            "],\n" +
                            "\"projectId\": \""+projectId+"\",\n" +
                            "\"publicEndpoints\": [ ],\n" +
                            "\"publishNotReadyAddresses\": false,\n" +
                            "\"selector\": {\n" +
                            "\"app\": \"grafana\",\n" +
                            "\"release\": \""+appId+"\"\n" +
                            "},\n" +
                            "\"sessionAffinity\": \"None\",\n" +
                            "\"sessionAffinityConfig\": null,\n" +
                            "\"state\": \"active\",\n" +
                            "\"targetDnsRecordIds\": [ ],\n" +
                            "\"targetWorkloadIds\": [ ],\n" +
                            "\"topologyKeys\": [ ],\n" +
                            "\"transitioning\": \"no\",\n" +
                            "\"transitioningMessage\": \"\",\n" +
                            "\"uuid\": \"\",\n" +
                            "\"workloadId\": null\n" +
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


    public String getApps(CatalogVo catalogVo, String bearerToken) {
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
            String result ="";
            result = webClient.get()         // GET method
                    .uri("/project/" + catalogVo.getProjectId() + "/services")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println("result : " + result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("state", dataObj.get("state"));
                returnObj.put("namespaceId", dataObj.get("namespaceId"));
                returnObj.put("selector", dataObj.get("selector"));
                returnObj.put("ports",dataObj.get("ports"));

                returnArr.put(returnObj);
            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";

        System.out.println("getServices >>>>> ");
        System.out.println(returnArr.toString());

        return returnArr.toString();
    }

}
