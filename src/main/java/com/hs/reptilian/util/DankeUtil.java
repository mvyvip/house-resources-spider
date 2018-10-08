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

    private static String token = "Zyo17NjWMHT6jQ6YMCoF77YRIrAUKcxkQoctRtgT";

    private static String cookie = "XSRF-TOKEN=eyJpdiI6Im4xRFlZSlhwXC9FXC9rNHkwNyszeStPdz09IiwidmFsdWUiOiJ1YmZnbU55dDk0VFJYMU5aYzJ3d3FpaWN1b1B3Z29PTUluU2VEcUkwc0pcL1wvVjRyY3FNMkd1K2NrRFgxRUxMZm83bWFcLzdUSEdBWjJaY3puOHV1SEYyQT09IiwibWFjIjoiZGE5ODY1ZmJjYzZhNzNlYzk1MDA5MGExOGM1ZDgxZTllY2MxY2RlZDViZGFiNjJiNjhiOWQ3YzQzYjQyOWUwMyJ9; session=eyJpdiI6InJVYXRNTnFxZWxTNzhoa3BpWndUXC9BPT0iLCJ2YWx1ZSI6IkJWSjlhOU8wR0V1VDRQVnk4YXNYQk1ZbFNic0dXUndCNU5MOWpwWGJSU0R3cTlKSFNlanBNYlZVNWVIb2JldU9NQUxrcXYrZW1DckpEZjY1Q3dsUUVBPT0iLCJtYWMiOiJhYzA5ZmRkZjUyZWM3MjY5YjIxNjA3OWFiNGIwNzVhOTY3ZTMxYjRiMTc4ODlmNzQyNDg0NDY4MzQ2YjQ4OTQ2In0%3D; Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1538906022; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1538898140; _ga=GA1.2.1909943659.1538901032; _gat=1; _gid=GA1.2.1213997825.1538901032; CNZZDATA1271579284=473856105-1538894229-%7C1538905026; externalHouseRecorderPassengerClose=true; UM_distinctid=1664d7a19e37a1-0103f291c17b9a8-664b2f6c-3d10d-1664d7a19e423e; TY_SESSION_ID=5f461ba3-9591-41b4-831d-7864c5bde1fc";

    /**
     * 上传数据到蛋壳
     * @param hsReportData
     */
    public static void report(HsReportData hsReportData) throws Exception {
        Integer compounId = serchCompoundId(hsReportData.getCity(), hsReportData.getCompoundName());
        if(compounId != null) {
            try {
//                Connection.Response execute = Jsoup.connect("https://www.dankegongyu.com/u/house-resource/insert")
//                        .method(Connection.Method.POST)
//                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                        .header("Cookie", cookie)
//                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
//                        .ignoreContentType(true)
//                        .timeout(10000)
//                        .header("Content-Type", "application/x-www-form-urlencoded")
//                        .data("_token", token)
//
////                    .data("city", "广州市")
////                .data("doorplate", "1")
////                .data("landlord_name", "测试不用管2")
////                .data("landlord_phone", "18027354329")
////                .data("xiaoqu_id", "47586")
//
//
//                        .data("city", hsReportData.getCity())
//                        .data("doorplate", "1")
//                        .data("landlord_name", hsReportData.getUsername())
//                        .data("landlord_phone", hsReportData.getMobile().trim())
////                        .data("landlord_phone", "15261822164")
//                        .data("xiaoqu_id", compounId.toString())
//                        .execute();
                System.err.println("x>>  " + hsReportData);
//                if(execute.body().contains("房源已成功提交")) {
//                    log.info("成功提交：" + hsReportData);
//                }
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
    private static Integer serchCompoundId(String city, String compoundName) throws Exception {
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
        return null;
    }

    public static void main(String[] args) throws Exception {
     /*   HsReportData hsReportData = new HsReportData();
        hsReportData.setCity("北京市");
        hsReportData.setUsername("林先生（个人）");
        hsReportData.setCompoundName("松榆西里");
        hsReportData.setMobile("18924313673");
        DankeUtil.report(hsReportData);*/

        Connection.Response execute = Jsoup.connect("https://api.anjuke.com/weixin/rent/property/view?cid=15&from=weapp&app=i-ajk&platform=ios&b=iPhone&s=iOS11.3.1&t=1538916837&cv=1.6&wcv=1.6&wv=6.7.3&sv=2.3.0&batteryLevel=100&oid=ocS7q0B3Meb_BKooeVwVtHpr-xc8&udid=ocS7q0B3Meb_BKooeVwVtHpr-xc8&id=81399715&create_type=2&expire=7776000&soj_flag=pc_zfdy&source_type=2&width=69&src=undefined&from_share=middle_page&city_id=13&json_data=%7B%7D&share=middle_page&height=69&isauction=1&ft=pc_zfdy&is_auction=1&cityid=13")
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
        System.out.println(execute.body());


    }

}
