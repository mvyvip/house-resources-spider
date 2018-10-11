package com.hs.reptilian;

import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.constant.UrlConstant.FangTianXia;
import com.hs.reptilian.model.HsReportData;
import com.hs.reptilian.util.DankeUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

import com.hs.reptilian.util.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by lt on 2018/10/11 0011.
 */
public class TestSpider {

    public static void main(String[] args) throws IOException {

        ProxyUtil proxyUtil = new ProxyUtil();

        Document document = Jsoup.connect("http://sh.ganji.com/fang1/a1m1/")
            .timeout(SystemConstant.TIME_OUT)
            .proxy(proxyUtil.getProxy())
            .execute().parse();

        if(document.toString().contains("访问过于频繁")) {
            //  TODO 换代理重新来
            System.out.println("换ip");
            return;

        }

        Elements dls = document.select("div.f-main.f-clear.f-w1190").get(0).getElementsByTag("dl");
        /** 第一个是广告 */
        dls.remove(0);
        for (Element dl : dls) {
            // TODO 暂时只爬个人的 58的后面添加  这里会返回三种房源 公寓 个人 和 58的
            String href = dl.getElementsByTag("a").get(1).attr("href");
            if(!href.contains("legoc")) {
                String id = "";
                if(href.contains("short")) {
//                    System.out.println(dl);
                    String[] split = dl.toString().split("}}}")[0].split("\\{");
                    id = split[split.length - 1].split(":")[1] + "x";
                } else {
                    id = href.split("/")[2].split("\\.")[0];
                }
                System.out.println("http://3g.ganji.com/hz_fang1/" + id);
                Document infoDc = Jsoup.connect("http://3g.ganji.com/hz_fang1/" + id)
                        .timeout(SystemConstant.TIME_OUT)
//                        .proxy(proxyUtil.getProxy())
                        .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("221.223.91.178", 8060)))
                        .execute().parse();

                /***



                 :

                 *
                 */

//                System.out.println(infoDc);
                if(infoDc.toString().contains("访问过于频繁")) {
                    System.out.println("访问过于频繁");
                    continue;
                }

                if(!infoDc.toString().contains("data-encryption")) {
                    System.out.println("特殊  要登录才能看");
                    continue;
                }

                Elements tagA = infoDc.getElementsByTag("a");
                for (Element element : tagA) {
                    if(StringUtils.isNotEmpty(element.attr("data-encryption"))) {
                        HsReportData hsReportData = new HsReportData();
                        String body = Jsoup.connect("http://3g.ganji.com/ajax/?module=secret_GetSecretPhone&dir=secret&a=json&version=4&user_id=&" +
                                "phone=" + element.attr("data-encryption") +"&puid=" + id + "&major_index=fang1&safe_no=0")
                                .proxy(proxyUtil.getProxy())
                                .timeout(SystemConstant.TIME_OUT)
                                .execute().body();
                        Elements span = infoDc.getElementsByTag("span");
                        for (Element s : span) {
                            if(s.toString().contains("(个人)")) {
                                hsReportData.setUsername(s.text().split(" ")[0].replaceAll("\\(个人\\)", ""));
                            }
                        }
                        hsReportData.setMobile(JSONObject.parseObject(body).getString("secret_phone"));
                        hsReportData.setCompoundName(infoDc.text().split("小区：")[1].split(" ")[0].split("\\(")[0]);
                        System.out.println(hsReportData);
                        break;
                    }
                }
                System.out.println("-----------------------------------------------");
//                return;
            }
        }

    }

}
