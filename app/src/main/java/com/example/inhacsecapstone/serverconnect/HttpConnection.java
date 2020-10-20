package com.example.inhacsecapstone.serverconnect;
import okhttp3.OkHttpClient;

public class HttpConnection {
    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();
    private static final String portNumber = "5000";
    private static final String ipv4Address = "192.168.25.39";

    public static HttpConnection getInstance() {
        return instance;
    }
    private HttpConnection(){ this.client = new OkHttpClient(); }
    public String getUrl(String url) {
        // local host 동작을 사용할 시
        return "http://" + ipv4Address + ":" + portNumber + "/"+url;

        // ngrok을 이용한 port forwarding시
        //return "https://059b8dc121fe.ngrok.io/" + url;
    }
}
