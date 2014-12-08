package com.sap.sea.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class HttpResult {

    private int status;
    private long elapsed;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private String body;

    public HttpResult(String error) {
        status = 0;
        elapsed = 0;
        headers = new LinkedHashMap<String, String>();
        cookies = new LinkedHashMap<String, String>();
        body = error;
    }

    public HttpResult(CloseableHttpResponse response, long elapsed) {

        // status code
        status = response.getStatusLine().getStatusCode();

        // elapsed
        this.elapsed = elapsed;

        // headers
        headers = new LinkedHashMap<String, String>();
        Header[] list = response.getAllHeaders();
        for (Header header : list) {
            headers.put(header.getName(), header.getValue());
        }

        // cookies
        cookies = new LinkedHashMap<String, String>();
        list = response.getHeaders("Set-Cookie");
        for (Header header : list) {
            String[] tcc = header.getValue().split(";");
            String[] cc = tcc[0].split("=", 2);
            cookies.put(cc[0], cc.length == 2 ? cc[1] : "");
        }

        // body
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            body = e.getMessage();
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getContent() {
        return body.getBytes(Consts.UTF_8);
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
