package com.hs.reptilian.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.model.ProxyEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PandaProxyUtil {

    private static int count = 50;

    private static String url = "http://www.xiongmaodaili.com/xiongmao-web/api/glip?secret=6a53ee7885839ed1f1e2d3b11623b5dd&orderNo=GL20181016222432i85Vvusp&count=" + count + "&isTxt=0&proxyType=1";

    private static List<ProxyEntity> ips = new ArrayList<>();

    private static AtomicInteger index = new AtomicInteger(0);

    public synchronized static void init() throws IOException {
        String body = Jsoup.connect(url)
                .timeout(10000)
                .execute().body();
        JSONArray obj = JSONObject.parseObject(body).getJSONArray("obj");
        System.err.println("obj>>> " + obj + "  body: " + body);
        for (Object o : obj) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
            ProxyEntity proxyEntity = new ProxyEntity(jsonObject.getString("ip"), jsonObject.getInteger("port"), DateUtils.addMinutes(new Date(),5));
            ips.add(proxyEntity);
        }
    }

    public synchronized static Proxy getProxy() throws IOException {
        if(ips.size() < count) {
            init();
        }
        if(index.get() >= ips.size()) {
            index.set(0);
        }
        ProxyEntity proxyEntity = ips.get(index.get());
        index.incrementAndGet();
        log.info(proxyEntity.getIp() + "---" + proxyEntity.getPort());
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyEntity.getIp(), proxyEntity.getPort()));
    }

    public static void handlerError(Exception e, Proxy proxy) {
        if(e instanceof SocketTimeoutException && proxy != null) {
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            Iterator<ProxyEntity> iterator = ips.iterator();
            while (iterator.hasNext()) {
                ProxyEntity next = iterator.next();
                if(next.getIp().equals(socketAddress.getHostName())) {
                    iterator.remove();
                    log.info("移除代理： {}", next);
                }
            }
        }
    }

}
