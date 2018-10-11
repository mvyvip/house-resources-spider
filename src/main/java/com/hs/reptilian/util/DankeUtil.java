package com.hs.reptilian.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.model.HsReportData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Slf4j
@Component
public class DankeUtil {

    private static String token = "zMOlXOxUubPDsnjLJEFQDOkjYvxBx2fEtePsDxhF";

    private static String cookie = "XSRF-TOKEN=eyJpdiI6IkVuSmJSMnVjRVFra2R2bUxYNjNvK3c9PSIsInZhbHVlIjoiQXd5ZytNeGlucG5hTDdLdUxoQzd3ZlNHMndqN3huZFBBcWxvN1wvWWV1WHRZeDNSWTlhZ2xkOWtGcmRFZnFZR2R2ZkxOZEcwMHpjNjBcLzJEdzNxbHFuUT09IiwibWFjIjoiOGEzNWRjNzVlNzgzZTM4YTc3NTAyYTU2YjNjYjAwNDk4ZDgxOTZiYjZkZGIwMGVmZDhiODYxMTA4ZDgyY2U2ZSJ9; session=eyJpdiI6IlZBRGxlcnFEdnBIYXhtblwvempYSDRnPT0iLCJ2YWx1ZSI6IlFQUHRhS2s3TWw3dmpENTJNZmk2eXc5cE14R21DTEdvQjdwTUErbFkzYUVhZXdoc3VsK0EydjgzRWRwWWJBclwvZkRPQ1hrT29jdVBBRHhJRE5yTm9HQT09IiwibWFjIjoiYTJmYTU3NDVlNzU0ZTVkYzg1Y2ZhZWRjMmMyNzgzMDQxODhkMzlkZjI1YzllOWEwNjNlNjNhNTU0YmQ5Yjc2ZSJ9; Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1539182671; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1539182265; CNZZDATA1271579284=1755457723-1539182065-%7C1539182065; externalHouseRecorderPassengerClose=true; UM_distinctid=1665e698311111-0d628fef6846de-1f0a1108-3d10d-1665e698313cf; TY_SESSION_ID=0d1876e6-86bf-4ed2-bd87-31140a704b6b";

    /**
     * 上传数据到蛋壳
     * @param hsReportData
     */
    public static void report(HsReportData hsReportData) {
        Integer compounId = serchCompoundId(hsReportData.getCity(), hsReportData.getCompoundName());
        if(compounId != null) {
            try {
                Connection.Response execute = Jsoup.connect("https://www.dankegongyu.com/u/house-resource/insert")
                        .method(Connection.Method.POST)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Cookie", cookie)
                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
                        .ignoreContentType(true)
                        .timeout(10000)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .data("_token", token)

//                    .data("city", "广州市")
//                .data("doorplate", "1")
//                .data("landlord_name", "测试不用管2")
//                .data("landlord_phone", "18027354327")
//                .data("xiaoqu_id", "47586")


                        .data("city", hsReportData.getCity())
                        .data("doorplate", "1")
                        .data("landlord_name", hsReportData.getUsername())
                        .data("landlord_phone", hsReportData.getMobile().trim())
//                        .data("landlord_phone", "15261822164")
                        .data("xiaoqu_id", compounId.toString())
                        .execute();
                if(execute.body().contains("房源已成功提交")) {
                    System.err.println("成功提交：  " + hsReportData);
                }
//                System.err.println(execute.body());
            } catch (Exception e) {
//                e.printStackTrace();
                log.info("已被人提交：" + hsReportData);
            }

        }
    }

    /**
     * 查询小区在蛋壳中的id
     * @return
     */
    private static Integer serchCompoundId(String city, String compoundName) {
       try {
           Connection.Response execute = Jsoup.connect("https://www.dankegongyu.com/u/house-resource/auto-xiaoqu-name?city=" + URLEncoder.encode(city, "UTF-8") +"&q=" + URLEncoder.encode(compoundName, "UTF-8") + "&_=" + System.currentTimeMillis())
                   .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                   .header("Cookie", cookie)
                   .header("X-Requested-With", "XMLHttpRequest")
                   .header("X-Tingyun-Id", "HfvUoHu3rrU;r=835988035")
                   .header("X-CSRF-TOKEN", "KY4pkm2KZhx7DBNhpURSV3oaLGqD8uYFTrXXz1hx")
                   .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
                   .header("Content-Type", "application/x-www-form-urlencoded")
                   .ignoreContentType(true)
                   .execute();
           JSONArray jsonArray= JSONObject.parseArray(execute.body());
           for (Object obj : jsonArray) {
               JSONObject jsonObject = JSONObject.parseObject(obj.toString());
               if(jsonObject.getString("name").contains(compoundName)) {
                   return jsonObject.getInteger("id");
               }
           }
           log.info(city + "-------" + compoundName + "-------未找到小区id");
           return null;
       } catch (Exception e) {
            log.info("获取小区失败： " + city + "---" + compoundName);
            return serchCompoundId(city, compoundName);
       }
    }

    public static void main(String[] args) throws Exception {
       while (true) {
           try {
               HsReportData hsReportData = new HsReportData();
               hsReportData.setCity("北京市");
               hsReportData.setUsername("测试不打电话");
               hsReportData.setCompoundName("松榆西里");
               hsReportData.setMobile("18922353613");
               DankeUtil.report(hsReportData);
               Thread.sleep(60 * 1000 * 7);
           } catch (Exception e) {
                e.printStackTrace();
           }
       }

      /*  Connection.Response execute = Jsoup.connect("https://api.anjuke.com/weixin/rent/property/view?cid=15&from=weapp&app=i-ajk&platform=ios&b=iPhone&s=iOS11.3.1&t=1538916837&cv=1.6&wcv=1.6&wv=6.7.3&sv=2.3.0&batteryLevel=100&oid=ocS7q0B3Meb_BKooeVwVtHpr-xc8&udid=ocS7q0B3Meb_BKooeVwVtHpr-xc8&id=81399715&create_type=2&expire=7776000&soj_flag=pc_zfdy&source_type=2&width=69&src=undefined&from_share=middle_page&city_id=13&json_data=%7B%7D&share=middle_page&height=69&isauction=1&ft=pc_zfdy&is_auction=1&cityid=13")
                .header("X_AJK_APP", "i-weapp")
                .header("ak", "f8d5b75d0b3991fbcaf64de096d025537fb40f3e")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
                .header("X-Forwarded-For", "182.49.51.238")
                .header("sig", "a7ea463e09ee250d9458936832511b9b")
                .header("ft", "ajk-weapp")
                .header("Cookie", "CURRENT_VERSION=1.6; aQQ_ajkguid=ocS7q0B3Meb_BKooeVwVtHpr-xc8;")
                .ignoreContentType(true)
                .execute();
        System.out.println(execute.body());*/


    }

}
