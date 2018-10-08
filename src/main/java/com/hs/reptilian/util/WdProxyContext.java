package com.hs.reptilian.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.model.ProxyEntity;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

@Slf4j
@Component
public class WdProxyContext {


    private static volatile List<ProxyEntity> proxys = new ArrayList<>();

    private synchronized static Proxy getProxy() throws Exception {
        Iterator<ProxyEntity> iterator = workQueue.iterator();
        while (iterator.hasNext()) {
            ProxyEntity next = iterator.next();
            if (next.isExpire()) {
                iterator.remove();
            }
        }
        Collections.shuffle(workQueue);
        ProxyEntity proxyEntity = workQueue.get(0);
        System.out.println("proxy > " + proxyEntity.getIp() + ":"+ proxyEntity.getPort());
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyEntity.getIp(), proxyEntity.getPort()));
    }

    @Scheduled(cron = "${sync.hs.cron}")
    private void  initIps(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Connection.Response response = Jsoup.connect(SystemConstant.IP_URL)
                                .timeout(SystemConstant.TIME_OUT)
                                .ignoreContentType(true)
                                .header("Content-Type", "application/json; charset=UTF-8")
                                .execute();
                        System.out.println(response.body() + "----" + new Date().toLocaleString());
                        JSONArray datas = JSONObject.parseObject(response.body()).getJSONArray("data");
                        for (Object data : datas) {
                            JSONObject jsonObject = JSONObject.parseObject(data.toString());
                            workQueue.add(new ProxyEntity(jsonObject.getString("ip"), jsonObject.getInteger("port"), jsonObject.getDate("expire_time")));
                        }
                        System.out.println("初始化完毕" + new Date().toLocaleString());
                        Thread.sleep(60 * 3 * 1000);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage() + "-----" + new Date().toLocaleString());
                }
            }
        }).start();
    }



}
