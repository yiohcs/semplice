package com.yschoi.login.service;

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
import java.util.concurrent.TimeUnit;

@Service("loginService")
public class LoginService {

    public String apiGetService(String uri, String bearerToken) throws SSLException {
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
        System.out.println("Login Token>>>>> " + bearerToken);
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

    public String getToken(String bearerToken){
        String returnStr = "[";
        JSONArray returnArr = null;
        ClusterGroup clusterGroup = new ClusterGroup();

        try {
            String result = apiGetService("tokens", bearerToken);
            System.out.println(result);

            JSONObject json = null;
            json = new JSONObject(result);

            returnArr = new JSONArray();

            JSONArray dataArr = json.getJSONArray("data");
            for (int i = 0; i < dataArr.length(); i++) {
                JSONObject dataObj = dataArr.getJSONObject(i);
                JSONObject returnObj = new JSONObject();
                returnObj.put("description", dataObj.get("description"));
                returnObj.put("userId", dataObj.get("userId"));
                returnObj.put("name", dataObj.get("name"));

                returnArr.put(returnObj);
            }

            System.out.println("length: " + json.getJSONArray("data").length());
            System.out.println(returnArr);
            for (int i = 0; i < json.getJSONArray("data").length(); i++) {
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

    public String removeToken(String name, String bearerToken){
//        HTTP/1.1 DELETE /v3/tokens/token-6v57j
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
                    .uri("/tokens/" + name)    // baseUrl 이후 uri
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
