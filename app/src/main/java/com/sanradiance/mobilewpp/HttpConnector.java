package com.sanradiance.mobilewpp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnector {

    public static HttpURLConnection connection(String urlAddress){
        try {
            URL url = new URL(urlAddress);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set properties
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setChunkedStreamingMode(0);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);

            conn.connect();
            //return connection
            return  conn;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
