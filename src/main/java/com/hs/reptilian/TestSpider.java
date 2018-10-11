package com.hs.reptilian;

import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.constant.UrlConstant.FangTianXia;
import com.hs.reptilian.model.HsReportData;
import com.hs.reptilian.util.DankeUtil;
import java.io.IOException;
import java.util.Arrays;

import com.hs.reptilian.util.ProxyUtil;
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
                System.out.println("http://3g.ganji.com/hz_fang1/" + href.split("/")[2].split("\\.")[0]);
                Document infoDc = Jsoup.connect("http://3g.ganji.com/hz_fang1/" + href.split("/")[2].split("\\.")[0])
                    .timeout(SystemConstant.TIME_OUT)
                        .proxy(proxyUtil.getProxy())
                    .execute().parse();
                System.out.println(infoDc);
                System.out.println("-----------------------------------------------");
                return;
            }
        }

    }

}
