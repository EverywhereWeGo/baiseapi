package com.bfd;

/**
 * @author everywherewego
 * @date 1/25/21 9:44 AM
 */

public class Main {
    public static void main(String[] args) {
//        int pageSize = 100;
//        Map<String, String> requestHeaders = new HashMap<>();
//        requestHeaders.put("Accept", "*/*");
//        requestHeaders.put("accept-encoding", "gzip, deflate");
//
//        JSONObject param = new JSONObject();
//        JSONArray params = new JSONArray();
//        for (int i = 0; ; i++) {
//            JSONObject jsonObject = new JSONObject();
//
//            JSONArray jsonArray = querySql("select object_name, object_business_code from object_info order by id limit " + i * pageSize + " , " + pageSize);
//            for (int j = 0; j < jsonArray.size(); j++) {
//                String name = jsonArray.getJSONObject(j).getString("object_name");
//                String idcard = jsonArray.getJSONObject(j).getString("object_business_code");
//                String base64String = sendGetToGetPicture("http://10.236.54.235:13734/img?SFZH=" + idcard, requestHeaders);
//                if (null == base64String) {
//                    continue;
//                }
//                jsonObject.put("repository_id", "1");
//                jsonObject.put("picture_image_content_base64", base64String);
//                jsonObject.put("person_id", idcard);
//                params.add(jsonObject);
//            }
//            param.put("images", params);
//            insertsinglepic(param);
//            if (jsonArray.size() < pageSize) {
//                break;
//            }
//        }
        System.out.println("jenkisc测试");
        System.out.println("jenkisc测试2");
        System.out.println("jenkisc测试3");
        System.out.println("jenkisc测试4");
        System.out.println("jenkisc测试5");
        System.out.println("jenkisc测试6");
        System.out.println("jenkisc测试7");
        int i=0;
        while (true){
            System.out.println(i++);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
