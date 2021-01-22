package com.yschoi.node.service;

import com.yschoi.cluster.model.ClusterVo;
import com.yschoi.node.model.NodeVo;
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

@Service("nodeService")
public class NodeService {
    public String getNodes(String clusterId, String bearerToken) {
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
                    .uri("/nodes/")    // baseUrl 이후 uri
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

//            returnArr = new JSONArray();
//
//            JSONArray dataArr = json.getJSONArray("data");
//            for (int i = 0; i < dataArr.length(); i++) {
//                JSONObject dataObj = dataArr.getJSONObject(i);
//                JSONObject returnObj = new JSONObject();
//                returnObj.put("id", dataObj.get("id"));
//                returnObj.put("state", dataObj.get("state"));
//                returnObj.put("name", dataObj.get("name"));
//
//                returnArr.put(returnObj);
//            }
//            System.out.println("length: " + json.getJSONArray("data").length());
//            System.out.println(returnArr);
            boolean first = true;

            for (int i = 0; i < json.getJSONArray("data").length(); i++) {

                if(json.getJSONArray("data").getJSONObject(i).get("clusterId").toString().equals(clusterId)){
                    if (first) {
                        returnStr += json.getJSONArray("data").getJSONObject(i).toString();
                        first = false;
                    }
                    else returnStr += "," + json.getJSONArray("data").getJSONObject(i).toString();
                }
            }
            System.out.println(returnStr);

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";



        return returnStr;
    }


    public String modifyNodeName(NodeVo nodeVo, String bearerToken) {
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
                    .uri("/nodes/" + nodeVo.getId())    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .contentLength(299)
                    .bodyValue("{\n" +
                            "\"name\": \""+nodeVo.getName()+"\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await

            System.out.println("modifyNodeName >>>>>>>>>> ");
            System.out.println(result);
            return result;
        }catch(SSLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getClusterToken(NodeVo nodeVo,String bearerToken) {
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
                    .uri("/clusterregistrationtokens")    // baseUrl 이후 uri
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + bearerToken)
                    .retrieve()                 // client message 전송
                    .bodyToMono(String.class)  // body type : EmpInfo
                    .block();                   // await
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);



            returnArr = json.getJSONArray("data");
            for (int i = 0; i < returnArr.length(); i++) {
                if(returnArr.getJSONObject(i).get("clusterId").toString().equals(nodeVo.getId())){
                    returnStr += returnArr.getJSONObject(i).toString();
                    break;
                }
            }

        }catch (SSLException | JSONException e){
            e.printStackTrace();
        }

        returnStr += "]";

        System.out.println("returnStr >>> "+returnStr);

        return returnStr;
    }

    public String deleteNode(NodeVo nodeVo, String bearerToken) {
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
                    .uri("/nodes/" + nodeVo.getId())    // baseUrl 이후 uri
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
}
