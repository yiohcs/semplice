package com.yschoi.cluster.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yschoi.cluster.model.ClusterGroup;
import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.cluster.model.CommonVo;
import com.yschoi.demo.model.EmpInfoGroup;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("clusterService")
public class ClusterService {

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

        WebClient webClient = WebClient.builder().baseUrl("https://89.98.90.174/v3").clientConnector(httpConnector)

                .build();
        // httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);
//        System.out.println("Cluster Token>>>>> " + bearerToken);
        String result = webClient.get()         // GET method
                .uri("/" + uri + "?sort=state")    // baseUrl 이후 uri
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()                 // client message 전송
                .bodyToMono(String.class)  // body type : EmpInfo
                .block();                   // await
        System.out.println("result : " + result);

        return result;
    }

    public String getClusters(String bearerToken) {
        String returnStr = "[";
        JSONArray returnArr = null;
        ClusterGroup clusterGroup = new ClusterGroup();

        try {
            String result = apiGetService("clusters", bearerToken);
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

//            returnArr = new JSONArray();

//            JSONArray dataArr = json.getJSONArray("data");
//            for (int i = 0; i < dataArr.length(); i++) {
//                JSONObject dataObj = dataArr.getJSONObject(i);
//                JSONObject returnObj = new JSONObject();
//                returnObj.put("id", dataObj.get("id"));
//                returnObj.put("state", dataObj.get("state"));
//                returnObj.put("name", dataObj.get("name"));
//                returnObj.put("provider", dataObj.get("provider"));
//                returnObj.put("nodeCount", dataObj.get("nodeCount"));
//                returnObj.put("created", dataObj.get("created"));
//                returnObj.put("appliedSpec", dataObj.get("appliedSpec"));
//
//                returnObj.put("version", dataObj.getJSONObject("version"));
//                returnObj.put("requested", dataObj.getJSONObject("requested"));
//                returnObj.put("capacity", dataObj.getJSONObject("capacity"));
//
//                returnArr.put(returnObj);
//            }
//            System.out.println("length: " + json.getJSONArray("data").length());
//            System.out.println(returnArr);
            for (int i = 0; i < json.getJSONArray("data").length(); i++) {
                if(i==0) returnStr += json.getJSONArray("data").getJSONObject(i).toString();
                else returnStr += "," + json.getJSONArray("data").getJSONObject(i).toString();
            }

//            List<ClusterVo> list = new ArrayList<>();
//            for(int i=0;i<returnArr.length();i++){
//                ClusterVo clusterVo = new ClusterVo();
//                CommonVo commonVo = new CommonVo ();
//                commonVo.setCpu(returnArr.getJSONObject(i).getJSONObject("requested").get("cpu").toString());
//                commonVo.setMemory(returnArr.getJSONObject(i).getJSONObject("requested").get("memory").toString());
//                commonVo.setMemory(returnArr.getJSONObject(i).getJSONObject("requested").get("pods").toString());
//                clusterVo.setRequested(commonVo);
//
//                clusterVo.setState(returnArr.getJSONObject(i).get("state").toString());
//                clusterVo.setName(returnArr.getJSONObject(i).get("name").toString());
//                clusterVo.setNodeCount(Integer.parseInt(returnArr.getJSONObject(i).get("nodeCount").toString()));
//                clusterVo.setProvider(returnArr.getJSONObject(i).get("provider").toString());
//                list.add(clusterVo);
//            }
//            clusterGroup.setEmpInfos(list);

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";



        return returnStr;
    }

    public String getNode(String nodeId,String bearerToken) {
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
                    .uri("/nodes/" + nodeId)    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = json.getJSONArray("");
            for (int i = 0; i < json.length(); i++) {
                if(i==0) returnStr += returnArr.getJSONObject(i).toString();
                else returnStr += "," + returnArr.getJSONObject(i).toString();
            }

//            List<ClusterVo> list = new ArrayList<>();
//            for(int i=0;i<returnArr.length();i++){
//                ClusterVo clusterVo = new ClusterVo();
//                CommonVo commonVo = new CommonVo ();
//                commonVo.setCpu(returnArr.getJSONObject(i).getJSONObject("requested").get("cpu").toString());
//                commonVo.setMemory(returnArr.getJSONObject(i).getJSONObject("requested").get("memory").toString());
//                commonVo.setMemory(returnArr.getJSONObject(i).getJSONObject("requested").get("pods").toString());
//                clusterVo.setRequested(commonVo);
//
//                clusterVo.setState(returnArr.getJSONObject(i).get("state").toString());
//                clusterVo.setName(returnArr.getJSONObject(i).get("name").toString());
//                clusterVo.setNodeCount(Integer.parseInt(returnArr.getJSONObject(i).get("nodeCount").toString()));
//                clusterVo.setProvider(returnArr.getJSONObject(i).get("provider").toString());
//                list.add(clusterVo);
//            }
//            clusterGroup.setEmpInfos(list);

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";



        return returnStr;
    }

    public String deleteCluster(String id, String bearerToken) {
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

    public String createCluster(String name, String bearerToken) {
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


//        CLUSTERRESPONSE=`curl -s 'https://127.0.0.1/v3/cluster' -H 'content-type: application/json' -H "Authorization: Bearer $APITOKEN" --data-binary '{"type":"cluster","nodes":[],"rancherKubernetesEngineConfig":{"ignoreDockerVersion":true},"name":"yournewcluster"}

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
            String result = "";
            JSONObject json = null;
            String clusterId = "";
            result = webClient.post()         // POST method
                        .uri("/clusters")    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\n" +
                                "\"type\":\"cluster\",\n" +
                                "\"nodes\":[],\n" +
                                "\"rancherKubernetesEngineConfig\":{\"ignoreDockerVersion\":true},\n" +
                                "\"name\":\"" + name + "\"\n" +
                                "}")
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : String
                        .block();                   // await

            json = new JSONObject(result);

            clusterId = json.get("id").toString();

            //////// generationToken

                result = webClient.post()         // POST method
                        .uri("/clusterregistrationtokens")    // baseUrl 이후 uri
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(533)
                        .bodyValue("{\n" +
                                "\"clusterId\": \"" + clusterId + "\",\n" +
                                "\"name\": \"\",\n" +
                                "\"namespaceId\": \"\"\n" +
                                "}")
                        .header("Authorization", "Bearer " + bearerToken)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await

            return result;
        }catch(SSLException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }


    public String generateToken(String name, String bearerToken) {

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
                    .uri("/clusterregistrationtokens")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(533)
                    .bodyValue("{\n" +
                            "\"clusterId\": \""+name+"\",\n" +
                            "\"name\": \"\",\n" +
                            "\"namespaceId\": \"\"\n" +
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


    public String modifyClusterName(ClusterVo clusterVo, String bearerToken) {
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

            String result = webClient.put()         // POST method
                    .uri("/clusters/" + clusterVo.getId())    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(299)
                    .bodyValue("{\n" +
                            "\"name\": \""+clusterVo.getName()+"\"\n" +
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
}



//{
//        "amazonElasticContainerServiceConfig": null,
//        "annotations": {
//        "authz.management.cattle.io/creator-role-bindings": "{\"created\":[\"cluster-owner\"],\"required\":[\"cluster-owner\"]}",
//        "lifecycle.cattle.io/create.cluster-agent-controller-cleanup": "true",
//        "lifecycle.cattle.io/create.cluster-provisioner-controller": "true",
//        "lifecycle.cattle.io/create.cluster-scoped-gc": "true",
//        "lifecycle.cattle.io/create.mgmt-cluster-rbac-remove": "true",
//        "provisioner.cattle.io/ke-driver-update": "uadate"
//        },
//        "answers": null,
//        "azureKubernetesServiceConfig": null,
//        "clusterTemplateRevisionId": "",
//        "defaultClusterRoleForProjectMembers": "",
//        "defaultPodSecurityPolicyTemplateId": "",
//        "dockerRootDir": "/var/lib/docker",
//        "eksConfig": null,
//        "enableClusterAlerting": false,
//        "enableClusterMonitoring": false,
//        "fleetWorkspaceName": "fleet-test",
//        "googleKubernetesEngineConfig": null,
//        "k3sConfig": null,
//        "localClusterAuthEndpoint": null,
//        "name": "test",
//        "rancherKubernetesEngineConfig": null,
//        "rke2Config": null,
//        "scheduledClusterScan": null,
//        "windowsPreferedCluster": false
//}








//HTTP/1.1 POST /v3/clusters
//        Host: 89.98.90.174
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 3110
//
//        {
//        "agentImageOverride": "rancher/rancher-agent:v2.5.3",
//        "amazonElasticContainerServiceConfig": null,
//        "annotations": {
//        "authz.management.cattle.io/creator-role-bindings": "{\"created\":[\"cluster-owner\"],\"required\":[\"cluster-owner\"]}",
//        "lifecycle.cattle.io/create.cluster-agent-controller-cleanup": "true",
//        "lifecycle.cattle.io/create.cluster-provisioner-controller": "true",
//        "lifecycle.cattle.io/create.cluster-scoped-gc": "true",
//        "lifecycle.cattle.io/create.mgmt-cluster-rbac-remove": "true",
//        "provisioner.cattle.io/ke-driver-update": "update"
//        },
//        "answers": null,
//        "azureKubernetesServiceConfig": null,
//        "clusterTemplateRevisionId": "",
//        "defaultClusterRoleForProjectMembers": "",
//        "defaultPodSecurityPolicyTemplateId": "",
//        "dockerRootDir": "/var/lib/docker",
//        "eksConfig": null,
//        "enableClusterAlerting": false,
//        "enableClusterMonitoring": false,
//        "googleKubernetesEngineConfig": null,
//        "k3sConfig": null,
//        "labels": {
//        "cattle.io/creator": "norman"
//        },
//        "localClusterAuthEndpoint": {
//        "enabled": true,
//        "type": "/v3/schemas/localClusterAuthEndpoint"
//        },
//        "name": "tttt",
//        "rancherKubernetesEngineConfig": {
//        "addonJobTimeout": 45,
//        "authentication": {
//        "strategy": "x509|webhook",
//        "type": "/v3/schemas/authnConfig"
//        },
//        "authorization": {
//        "type": "/v3/schemas/authzConfig"
//        },
//        "bastionHost": {
//        "sshAgentAuth": false,
//        "type": "/v3/schemas/bastionHost"
//        },
//        "cloudProvider": {
//        "type": "/v3/schemas/cloudProvider"
//        },
//        "dns": {
//        "nodelocal": {
//        "ipAddress": "",
//        "type": "/v3/schemas/nodelocal"
//        },
//        "type": "/v3/schemas/dnsConfig"
//        },
//        "ignoreDockerVersion": true,
//        "ingress": {
//        "httpPort": 0,
//        "httpsPort": 0,
//        "provider": "nginx",
//        "type": "/v3/schemas/ingressConfig"
//        },
//        "kubernetesVersion": "v1.19.4-rancher1-1",
//        "monitoring": {
//        "provider": "metrics-server",
//        "replicas": 1,
//        "type": "/v3/schemas/monitoringConfig"
//        },
//        "network": {
//        "mtu": 0,
//        "options": {
//        "flannel_backend_type": "vxlan"
//        },
//        "plugin": "canal",
//        "type": "/v3/schemas/networkConfig"
//        },
//        "restore": {
//        "restore": false,
//        "type": "/v3/schemas/restoreConfig"
//        },
//        "services": {
//        "etcd": {
//        "backupConfig": {
//        "enabled": true,
//        "intervalHours": 12,
//        "retention": 6,
//        "s3BackupConfig": null,
//        "safeTimestamp": false,
//        "type": "/v3/schemas/backupConfig"
//        },
//        "creation": "12h",
//        "extraArgs": {
//        "election-timeout": "5000",
//        "heartbeat-interval": "500"
//        },
//        "gid": 0,
//        "retention": "72h",
//        "snapshot": false,
//        "type": "/v3/schemas/etcdService",
//        "uid": 0
//        },
//        "kubeApi": {
//        "alwaysPullImages": false,
//        "podSecurityPolicy": false,
//        "serviceNodePortRange": "30000-32767",
//        "type": "/v3/schemas/kubeAPIService"
//        },
//        "kubeController": {
//        "type": "/v3/schemas/kubeControllerService"
//        },
//        "kubelet": {
//        "failSwapOn": false,
//        "generateServingCertificate": false,
//        "type": "/v3/schemas/kubeletService"
//        },
//        "kubeproxy": {
//        "type": "/v3/schemas/kubeproxyService"
//        },
//        "scheduler": {
//        "type": "/v3/schemas/schedulerService"
//        },
//        "type": "/v3/schemas/rkeConfigServices"
//        },
//        "sshAgentAuth": false,
//        "type": "/v3/schemas/rancherKubernetesEngineConfig",
//        "upgradeStrategy": {
//        "drain": false,
//        "maxUnavailableControlplane": "1",
//        "maxUnavailableWorker": "10%",
//        "nodeDrainInput": {
//        "deleteLocalData": false,
//        "force": false,
//        "gracePeriod": -1,
//        "ignoreDaemonSets": true,
//        "timeout": 120,
//        "type": "/v3/schemas/nodeDrainInput"
//        },
//        "type": "/v3/schemas/nodeUpgradeStrategy"
//        }
//        },
//        "rke2Config": null,
//        "scheduledClusterScan": {
//        "enabled": false,
//        "type": "/v3/schemas/scheduledClusterScan"
//        },
//        "windowsPreferedCluster": false
//        }