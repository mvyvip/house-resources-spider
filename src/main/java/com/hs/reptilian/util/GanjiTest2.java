package com.hs.reptilian.util;

import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant.GanJiWang;
import java.net.InetSocketAddress;
import java.net.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by lt on 2018/10/19 0019.
 */
public class GanjiTest2 {

    public static void main(String[] args) throws Exception {
        while (true) {
            Thread.sleep(1000);
            System.out.println(getCityInfo(GanJiWang.GZ.getUrl(), 0));
        }

    }

    private static Elements getCityInfo(String url, int retryCount) {
        try {
            Document document = Jsoup.connect(url)
                .timeout(SystemConstant.TIME_OUT)
//                .proxy(proxy)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
                .execute().parse();
            if(document.toString().contains("访问过于频繁")) {
                System.out.println("获取城市列表代理被封");
                return getCityInfo(url, 3);
            } else {
                System.out.println("===获取城市列表代理成功");
            }
            Elements dls = document.select("div.f-main.f-clear.f-w1190").get(0).getElementsByTag("dl");
            dls.remove(0);
            return dls;
        } catch (Exception e) {
            if(retryCount > 0) {
                return getCityInfo(url, --retryCount);
            }
            System.out.println("获取：{} -- 超过最大次数： {}");
            return null;
        }
    }


}
