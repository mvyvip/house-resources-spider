package com.hs.reptilian.task.runnable;

import com.hs.reptilian.ProxyDemo;
import com.hs.reptilian.constant.SysConstant;
import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.model.HsReportData;
import com.hs.reptilian.model.ReptilianList;
import com.hs.reptilian.util.DankeUtil;
import com.hs.reptilian.util.MessageFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.URLEncoder;
import java.util.Date;

@Slf4j
public class ReptilianRunnable implements Runnable {


    private ReptilianList reptilianList;

    private int MAX_RETRY = 3;

    private ThreadPoolTaskExecutor taskExecutor;

    public ReptilianRunnable(ReptilianList reptilianList, ThreadPoolTaskExecutor taskExecutor) {
        this.reptilianList = reptilianList;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run()  {
        try {
            log.info(Thread.currentThread().getName() + "  start........................." + new Date().toLocaleString());
            String url = MessageFormatUtil.format(UrlConstant.Dysj.getUrl(reptilianList.getCity()), URLEncoder.encode(reptilianList.getVillage(), "UTF-8"));
            Document document = Jsoup.connect(url)
                    .header(ProxyDemo.ProxyHeadKey, ProxyDemo.ProxyHeadVal).proxy(ProxyDemo.proxy)
                    .timeout(SysConstant.TIME_OUT)
                    .execute().parse();
            if(document.body().text().contains("很抱歉，没有找到与")) {
                log.info(reptilianList.getVillage() + "---" + reptilianList.getCity() + "---未找到房源");
                return;
            }

            Elements elements = document.getElementById("list").getElementsByTag("li");
            for (Element element : elements) {
                // TODO 异常重试
                HsReportData hsReportData = buidHsReportData(element.getElementsByTag("a").attr("href"));
                if(hsReportData != null) {
                    hsReportData.setCity(reptilianList.getCity());
                    DankeUtil.report(hsReportData);
                    System.out.println(hsReportData + " xxx  " + reptilianList);
                }
            }

        } catch (Exception e) {
            log.error("初始化房源信息出错："  + e.getMessage());
        }
            log.info(Thread.currentThread().getName() + "  end........................." + new Date().toLocaleString());
    }

    private HsReportData buidHsReportData(String url) {
        try {
            Document document = Jsoup.connect("http://gz.01fy.cn/rent/" + url)
                    .header(ProxyDemo.ProxyHeadKey, ProxyDemo.ProxyHeadVal).proxy(ProxyDemo.proxy)
                    .timeout(20000)
                    .execute().parse();
//        System.out.println(response.body());
            if(document.body().text().contains("请您输入验证码")) {
                log.info("该代理已被禁，重试中");
                return buidHsReportData(url);
            }

            HsReportData hsReportData = new HsReportData();
            Elements dls = document.getElementsByClass("cr_left").get(0).getElementsByTag("dl");
//            System.out.println(document.text());
            for (Element dl : dls) {
                String text = dl.text();

                if(text.contains("租金价格")) {
                    //TODO 价格过滤
                }

                if(text.contains("小区名称")) {
                    hsReportData.setCompoundName(text.split("小区名称： ")[1]);
                }  else if(text.contains("联系人")) {
                    hsReportData.setUsername(text.split("联系人： ")[1]);
                } else if(text.contains("联系电话")) {
                    String mobile = text.split("联系电话： ")[1].replaceAll(" ", "");
                    if(mobile.contains("该房源来自58同城网")) {
                        return null;
                    }
                    hsReportData.setMobile(mobile);
                }
            }
            return hsReportData;
        } catch (Exception e) {
            if(MAX_RETRY > 0 ){
                --MAX_RETRY;
                return buidHsReportData(url);
            }
            return null;
        }
    }

}
