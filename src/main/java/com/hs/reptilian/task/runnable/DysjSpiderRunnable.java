package com.hs.reptilian.task.runnable;

import com.hs.reptilian.ProxyDemo;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.model.HsReportData;
import com.hs.reptilian.util.DankeUtil;
import com.hs.reptilian.util.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;

@Slf4j
public class DysjSpiderRunnable implements Runnable {

    private int MAX_RETRY = 3;

    private UrlConstant.Dysj dysj;

    private ProxyUtil proxyUtil;

    public DysjSpiderRunnable(UrlConstant.Dysj dysj, ProxyUtil proxyUtil) {
        this.dysj = dysj;
        this.proxyUtil = proxyUtil;
    }

    private Document getMainPage() {
        try {
            Document document = Jsoup.connect(dysj.getUrl())
                    .proxy(proxyUtil.getProxy())
                    .timeout(SystemConstant.TIME_OUT)
                    .execute().parse();
            return document;
        } catch (Exception e) {
            log.error(e.getMessage());
            return getMainPage();
        }
    }

    @Override
    public void run()  {
            log.info(Thread.currentThread().getName() + "  start........................." + new Date().toLocaleString() + "----" + dysj.name());
            Document document = getMainPage();
            Elements elements = document.getElementById("list").getElementsByTag("li");
            for (Element element : elements) {
                // TODO 异常重试
                HsReportData hsReportData = buidHsReportData(element.getElementsByTag("a").attr("href"));
                System.out.println(">>>> " + hsReportData);
                if(hsReportData != null) {
                    hsReportData.setCity(dysj.getCity());
                    DankeUtil.report(hsReportData);
                }
            }

            log.info(Thread.currentThread().getName() + "  end........................." + new Date().toLocaleString());
    }

    private HsReportData buidHsReportData(String url) {
        try {
            Proxy proxy = proxyUtil.getProxy();
            Document document = Jsoup.connect("http://gz.01fy.cn/rent/" + url)
                    .proxy(proxy)
                    .timeout(20000)
                    .execute().parse();
            if(document.body().text().contains("请您输入验证码")) {
                log.info("该代理已被禁，重试中");
                proxyUtil.remove(proxy);
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
                    if(mobile.contains("该房源来自58同城网") || mobile.contains("隐藏电话")) {
                        return null;
                    }
                    hsReportData.setMobile(mobile);
                }
            }
            return hsReportData;
        } catch (Exception e) {
            if(e instanceof HttpStatusException) {
                HttpStatusException httpStatusException = (HttpStatusException) e;
                log.info("初始化数据出错：" + httpStatusException.getStatusCode() + "--" + httpStatusException.getUrl() + "---" + httpStatusException.getMessage());
                if(httpStatusException.getStatusCode() == 404) {
                    return null;
                }
            }
            log.error("初始化上报数据出错：" + e.getMessage());
            return buidHsReportData(url);
        }
    }

}
