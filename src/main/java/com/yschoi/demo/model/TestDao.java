package com.yschoi.demo.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TestDao {

    public void testPrint(){
        System.out.println("testPrint()");
    }

    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url){
        try{
            URLConnection connection = new URL(url).openConnection();
            HttpsURLConnection httpConn = (HttpsURLConnection) connection;
            //httpConn.setRequestProperty("Authorization", "Bearer " + rancherBearerToken);

            InputStream is = httpConn.getInputStream();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            }catch(JSONException ex){
                //Logger.getLogger(RancherClient.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                is.close();
            }
        }catch(IOException ex){
            //Logger.getLogger(RancherClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
