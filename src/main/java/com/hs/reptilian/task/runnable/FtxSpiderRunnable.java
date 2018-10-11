package com.hs.reptilian.task.runnable;

import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.constant.UrlConstant.FangTianXia;
import com.hs.reptilian.model.HsReportData;
import com.hs.reptilian.util.DankeUtil;
import com.hs.reptilian.util.ProxyUtil;
import java.net.Proxy;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class FtxSpiderRunnable implements Runnable {

    private int MAX_RETRY = 3;

    private UrlConstant.FangTianXia fangTianXia;

    private ProxyUtil proxyUtil;

    public FtxSpiderRunnable(FangTianXia dysj, ProxyUtil proxyUtil) {
        this.fangTianXia = dysj;
        this.proxyUtil = proxyUtil;
    }

    private Elements getMainPage() {
        try {
            Document document = Jsoup.connect(fangTianXia.getUrl())
                .timeout(SystemConstant.TIME_OUT)
                .proxy(proxyUtil.getProxy())
                .execute().parse();
            return document.getElementsByClass("houseList").get(0).getElementsByTag("dl");
        } catch (Exception e) {
            log.error(e.getMessage());
            return getMainPage();
        }
    }

    @Override
    public void run()  {
            try {
                System.out.println(fangTianXia + "---" + fangTianXia.getCityPrefix());
                Elements dls = getMainPage();
                for (Element dl : dls) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Document info = getInfo(dl);
                                JSONObject jsonObject = JSONObject.parseObject(info.html().split("houseInfo = ")[1].split(";")[0].replaceAll("// new  add by wys", ""));
                                if(!jsonObject.getString("agentMobile").contains("转")) {
                                    HsReportData hsReportData = new HsReportData();
                                    hsReportData.setCity(fangTianXia.getCity());
                                    hsReportData.setCompoundName(jsonObject.getString("projname").replaceAll("\\*", ""));
                                    hsReportData.setMobile(jsonObject.getString("agentMobile"));
                                    hsReportData.setUsername(jsonObject.getString("agentName"));
                                    System.out.println(hsReportData);
                                    DankeUtil.report(hsReportData);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
    }

    private Document getInfo(Element dl) {
        try {
            Document document = Jsoup.connect("http://" + fangTianXia.getCityPrefix() + ".zu.fang.com" + dl.getElementsByTag("a").get(0).attr("href"))
                .timeout(SystemConstant.TIME_OUT)
                .proxy(proxyUtil.getProxy())
                .execute().parse();
            return document;
        } catch (Exception e) {
            log.error(e.getMessage());
            return getInfo(dl);
        }
    }

}
