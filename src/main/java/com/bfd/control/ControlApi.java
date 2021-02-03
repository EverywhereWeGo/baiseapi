package com.bfd.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfd.utils.e_MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.bfd.utils.HttpClientHelper.sendPost;

/**
 * @author everywherewego
 * @date 1/25/21 5:20 PM
 */

public class ControlApi {
    private static Logger logger = LoggerFactory.getLogger(ControlApi.class);
    public static Map<String, String> headers = new HashMap<>();


    public static JSONObject insertsinglepic(JSONObject jsonObject) {
        login();
        String re = sendPost("http://71.178.56.51:11180/business/api/repository/picture/batch_single_person", headers, null, jsonObject.toJSONString()).get("responseContext");
        return JSON.parseObject(re);
    }


    private static void login() {
        headers.put("Content-type", "application/json");

        JSONObject body = new JSONObject();
        body.put("name", "admin");
        body.put("password", e_MD5Util.encrypt("Yitu@123"));

        String responseContext = sendPost("http://71.178.56.51:11180/business/api/login", headers, null, body.toString()).get("responseContext");
        JSONObject js = JSONObject.parseObject(responseContext);

        String sessionId = js.getString("session_id");

        headers.put("session_id", sessionId);
    }
}
