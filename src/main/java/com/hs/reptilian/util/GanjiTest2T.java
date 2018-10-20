package com.hs.reptilian.util;

import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.constant.UrlConstant.GanJiWang;
import com.hs.reptilian.model.HsReportData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by lt on 2018/10/19 0019.
 */
@Slf4j
public class GanjiTest2T {

    //    private static ProxyUtil proxyUtil = new ProxyUtil();
//
    private volatile static Set<String> set = new HashSet<>();
    private static Map<String, String> cookies = new HashMap<>();

    public static void main(String[] args) throws Exception {
        int count = 100000;
   /*     for (int i = 0; i < count; i++) {
            String randomIp = getRandomIp();

            System.err.println(randomIp);
        }
*/
                while (true) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Elements dls = getCityInfo(GanJiWang.GZ.getUrl(), 0);
                        System.out.println(dls.get(1).text());
                        for (int i = 0; i < 2; i++) {
                            int finalI = i;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Element dl = dls.get(finalI);
                                    String href = dl.getElementsByTag("a").get(1).attr("href");

                                    if (!href.contains("legoc")) {
                                        String id = "";
                                        if (href.contains("short")) {
                                            String[] split = dl.toString().split("}}}")[0].split("\\{");
                                            id = split[split.length - 1].split(":")[1] + "x";
                                        } else {
                                            id = href.split("/")[2].split("\\.")[0];
                                        }
                                        if(set.contains(id)) {
                                            System.out.println("爬取过了 停止继续");
                                        } else {
                                            set.add(id);
                                            System.out.println("http://3g.ganji.com/hz_fang1/" + id);
                                            Document infoDc = getHsInfo(id, SystemConstant.RETRY_COUNT);
                                            if (!infoDc.toString().contains("data-encryption")) {
                                                log.info("url: {}, 特殊  要登录才能看", "http://3g.ganji.com/hz_fang1/" + id);
                                            } else {
                                                Elements tagA = infoDc.getElementsByTag("a");
                                                for (Element element : tagA) {
                                                    if (StringUtils.isNotEmpty(element.attr("data-encryption"))) {
                                                        HsReportData hsReportData = new HsReportData();
                                                        String body = getPhoneBody(element, id, SystemConstant.RETRY_COUNT);
                                                        Elements span = infoDc.getElementsByTag("span");
                                                        for (Element s : span) {
                                                            if (s.toString().contains("(个人)")) {
                                                                hsReportData.setUsername(s.text().split(" ")[0].replaceAll("\\(个人\\)", ""));
                                                            }
                                                        }
                                                        hsReportData.setMobile(JSONObject.parseObject(body).getString("secret_phone"));
                                                        hsReportData.setCompoundName(infoDc.text().split("小区：")[1].split(" ")[0].split("\\(")[0]);
                                                        if (hsReportData != null) {
                                                            hsReportData.setCity(GanJiWang.GZ.getCity());
                                                            System.err.println(">>>> " + hsReportData);
                                                            DankeUtil.report(hsReportData);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }).start();
                        }
                    }
                }).start();
                    Thread.sleep(50);
            }




    }

    public static String getRandomIp() {
    // ip范围
    int[][] range = {{607649792, 608174079}, // 36.56.0.0-36.63.255.255
            {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
            {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
            {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
            {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
            {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
            {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
            {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
            {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
            {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
    };

    Random rdint = new Random();
    int index = rdint.nextInt(10);
    String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
		return ip;
}

    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }


    private static Elements getCityInfo(String url, int retryCount) {
        try {
            String randomIp = getRandomIp();
            Connection.Response response = null;
            response = Jsoup.connect(url)
                    .timeout(SystemConstant.TIME_OUT)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
                     .header("x-forward-for", randomIp)
                     .header("X-Forwarded-For", randomIp)
                     .header("client_ip", randomIp)
                     .header("CLIENT_IP", randomIp)
                     .header("REMOTE_ADDR", randomIp)
                     .header("VIA", randomIp)
                    .execute();

            Document document = response.parse();
            if (document.toString().contains("访问过于频繁")) {
                System.out.println("获取城市列表代理被封");
                return getCityInfo(url, 3);
            } else {
                System.out.println("===获取城市列表代理成功" + new Date().toLocaleString());
            }
            Elements dls = document.select("div.f-main.f-clear.f-w1190").get(0).getElementsByTag("dl");
            dls.remove(0);
            return dls;
        } catch (Exception e) {
            if (retryCount > 0) {
                return getCityInfo(url, --retryCount);
            }
            System.out.println("获取：{} -- 超过最大次数： {}");
            return null;
        }
    }

    public static String getPhoneBody(Element element, String id, int count) {
        try {
            String body = Jsoup.connect("http://3g.ganji.com/ajax/?module=secret_GetSecretPhone&dir=secret&a=json&version=4&user_id=&" +
                    "phone=" + element.attr("data-encryption") + "&puid=" + id + "&major_index=fang1&safe_no=0")
//                    .proxy(proxyUtil.getProxy())
                    .timeout(SystemConstant.TIME_OUT)
                    .execute().body();
            return body;
        } catch (Exception e) {
            if (count <= 0) {
                e.printStackTrace();
                return "";
            }
            return getPhoneBody(element, id, --count);
        }
    }

    private static Document getHsInfo(String id, int retryCount) {
        try {
            String randomIp = getRandomIp();
            Document parse = Jsoup.connect("http://3g.ganji.com/hz_fang1/" + id)
                    .timeout(SystemConstant.TIME_OUT)
                    .ignoreContentType(true)
                    .header("Accept:", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .header("Connection", "keep-alive")
                    .header("Cookie", "ganji_fang_fzp_m=1; ganji_uuid=9320612863215312812802; ganji_xuuid=50ad52cf-bb0d-459a-846e-5b93837b45d2.1539185116841; xxzl_deviceid=Ewl%2BKiSlWxbKb20oN4WrEvV8QBTrfNIN%2FprDu4Wp0OfKmdDKUss7kh30MawpLSnA; gj_footprint=%5B%5B%22%5Cu79df%5Cu623f%22%2C%22http%3A%5C%2F%5C%2Fcd.ganji.com%5C%2Ffang1%5C%2F%22%5D%5D; lg=1; ershoufangABTest=B; ganji_fang_fzp_pc=1; citydomain=sz; mobversionbeta=3g; __utmganji_v20110909=0xf2011350d9f1b0e8d8ffbd4fb7e9b53; UM_distinctid=16663420bb8505-00d3bd93bdbf87-2d604637-3d10d-16663420bb940d; cityDomain=hz; gj_recommend=%7B%22house%22%3A%5B%22%5Cu79df%5Cu623f%26nbsp%5Cu4e2a%5Cu4eba%26nbsp%5Cu6574%5Cu79df%22%2C%22http%3A%5C%2F%5C%2Fsh.ganji.com%5C%2Ffang1%5C%2Fa1m1%5C%2F%22%5D%7D; _gl_tracker=%7B%22ca_source%22%3A%22www.baidu.com%22%2C%22ca_name%22%3A%22-%22%2C%22ca_kw%22%3A%22-%22%2C%22ca_id%22%3A%22-%22%2C%22ca_s%22%3A%22seo_baidu%22%2C%22ca_n%22%3A%22-%22%2C%22ca_i%22%3A%22-%22%2C%22sid%22%3A81810219171%7D; GANJISESSID=03gqnrr5et65o51nnct6l0r35n; __utma=32156897.1900980152.1539186599.1539186599.1539614614.2; __utmc=32156897; __utmz=32156897.1539614614.2.2.utmcsr=sz.ganji.com|utmccn=(referral)|utmcmd=referral|utmcct=/; webimFangTips=3901839006; _wap__utmganji_wap_newCaInfo_V2=%7B%22ca_n%22%3A%22-%22%2C%22ca_s%22%3A%22self%22%2C%22ca_i%22%3A%22-%22%7D; _wap__utmganji_wap_caInfo_V2=%7B%22ca_name%22%3A%22-%22%2C%22ca_source%22%3A%22-%22%2C%22ca_id%22%3A%22-%22%2C%22ca_kw%22%3A%22-%22%7D; 4A387FC6C454795D14119CAB752410CEatxyeq3674=ee05f31c-cd7a-43f4-8a78-d2ae7f7f51c0; ti_idv208=1; CNZZDATA1274945977=247187861-1539262484-%7C1539613505; ganji_login_act=1539615711963; __utmt=1; __utmb=32156897.3.10.1539614614; ti_idv1093=10; ti_idv1184=7; ti_pdv28191=63; 4A387FC6C454795D14119CAB752410CEpdv28191=63; ti_idv1104=55; 4A387FC6C454795D14119CAB752410CEidvst=208%3A1%3A1539615751659%2C1104%3A55%3A1539615751659%2C1184%3A7%3A1539615751659; ti_pdv2891=39; ti_idv1202=39; ti_idv1095=11; ti_idvst=1202%3A39%3A1539615752008%2C1095%3A11%3A1539615752008%2C1093%3A10%3A1539615752008; xzfzqtoken=KQarrZvr0xNRGa9N6HW1eo17h5QKQ4dPw7ITLWaSVnVkftr6sbYjxALgESFltktMin35brBb%2F%2FeSODvMgkQULA%3D%3D")
                    .header("Host", "3g.ganji.com")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
//                    .proxy(proxyUtil.getProxy())

                     .header("x-forward-for", randomIp)
                    .header("X-Forwarded-For", randomIp)
                    .header("client_ip", randomIp)
                    .header("CLIENT_IP", randomIp)
                    .header("REMOTE_ADDR", randomIp)
                    .header("VIA", randomIp)
                    .execute().parse();
            if (parse.toString().contains("访问过于频繁")) {
                log.info("查看详情代理被封");
                return getHsInfo(id, retryCount);
            }
            System.out.println(parse.text());
            return parse;
        } catch (Exception e) {
            if (retryCount <= 0) {
                log.info("getHsInfo 超过错误最大次数: {}", e.getMessage());
                return null;
            }
            return getHsInfo(id, --retryCount);
        }
    }


}
