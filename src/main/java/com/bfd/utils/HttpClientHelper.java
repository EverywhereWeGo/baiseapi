package com.bfd.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangchong
 */
public class HttpClientHelper {
    public static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(100000);
        return httpClient;
    }

    public static Map<String, String> sendPost(String url, Map<String, String> requestHeaders, Map<String, String> params, String bdoy) {
        return sendPost(url, requestHeaders, params, bdoy, null);

    }

    public static Map<String, String> sendPost(String url, Map<String, String> requestHeaders, Map<String, String> params, String bdoy, Map<String, String> config) {
        Map<String, String> responseMap = new HashMap<String, String>(16);

        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(url);
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 100000);
        //设置请求头
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                postMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                postMethod.setParameter(entry.getKey(), entry.getValue());
            }
        }
        //设置requestbody
        if (null != bdoy) {
            postMethod.setRequestBody(bdoy);
        }

        String encodeType = "UTF-8";
        if (null != config) {
            encodeType = config.get("encodeType");
        }

        String result = "";
        try {
            httpClient.executeMethod(postMethod);

            StringBuilder cookiestr = new StringBuilder();
            Cookie[] cookies = httpClient.getState().getCookies();
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    cookiestr.append(cookie.toString()).append(";");
                }
                cookiestr.deleteCharAt(cookiestr.lastIndexOf(";"));
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), encodeType));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            responseMap.put("responseCookie", cookiestr.toString());
            responseMap.put("responseContext", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        postMethod.releaseConnection();
        return responseMap;
    }


    public static Map<String, String> sendGet(String url, Map<String, String> requestHeaders, Map<String, String> params, Map<String, String> config) {
        Map<String, String> responseMap = new HashMap<String, String>(16);
        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(url);
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                getMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        if (null != params) {
            HttpMethodParams pa = new HttpMethodParams();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pa.setParameter(entry.getKey(), entry.getValue());
            }
            getMethod.setParams(pa);
        }
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(getMethod);

            StringBuilder cookiestr = new StringBuilder();
            Cookie[] cookies = httpClient.getState().getCookies();
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    cookiestr.append(cookie.toString()).append(";");
                }
                cookiestr.deleteCharAt(cookiestr.lastIndexOf(";"));
            }

            String encodeType = "UTF-8";
            if (null != config) {
                encodeType = config.get("encodeType");
            }

            String result;
            BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), encodeType));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();


            responseMap.put("responseCookie", cookiestr.toString());
            responseMap.put("responseContext", result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        getMethod.releaseConnection();
        return responseMap;
    }

    /**
     * @param url
     * @param requestHeaders
     * @return //发送get请求获取图片
     */
    public static String sendGetToGetPicture(String url, Map<String, String> requestHeaders) {
        String s = null;
        Map<String, String> responseMap = new HashMap<String, String>(16);
        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(url);
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                getMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(getMethod);

            byte[] responseBody = getMethod.getResponseBody();
            if (responseBody.length <= 0) {
                return s;
            }
            Base64.Encoder encoder = Base64.getEncoder();
            s = encoder.encodeToString(responseBody);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        getMethod.releaseConnection();
        return s;
    }
}
