package com.hs.reptilian.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.reptilian.constant.SystemConstant;
import com.hs.reptilian.model.ProxyEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

@Slf4j
@Component
public class ProxyUtil {

    private List<ProxyEntity> proxyEntities = new ArrayList<>();

    public List<ProxyEntity> initIps() {
      try {
          Connection.Response response = Jsoup.connect(SystemConstant.IP_URL)
                  .timeout(SystemConstant.TIME_OUT)
                  .ignoreContentType(true)
                  .header("Content-Type", "application/json; charset=UTF-8")
                  .execute();
         log.info(response.body() + "----" + new Date().toLocaleString());
          JSONArray datas = JSONObject.parseObject(response.body()).getJSONArray("data");
          for (Object data : datas) {
              JSONObject jsonObject = JSONObject.parseObject(data.toString());
              proxyEntities.add(new ProxyEntity(jsonObject.getString("ip"), jsonObject.getInteger("port"), jsonObject.getDate("expire_time")));
          }
          log.info("初始化完毕" + new Date().toLocaleString());
      } catch (Exception e) {
          e.printStackTrace();
      }
        return null;
    }

    public synchronized Proxy getProxy() {
        if(getCanUsed() < SystemConstant.IP_COUNT) {
            initIps();
        }
        Iterator<ProxyEntity> iterator = proxyEntities.iterator();
        while (iterator.hasNext()) {
            ProxyEntity next = iterator.next();
            if (next.isExpire()) {
                iterator.remove();
            }
        }

        Collections.shuffle(proxyEntities);
        ProxyEntity proxyEntity = proxyEntities.get(0);
        log.info("proxy > " + proxyEntity.getIp() + ":"+ proxyEntity.getPort());
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyEntity.getIp(), proxyEntity.getPort()));
    }

    public synchronized void remove(Proxy proxy) {
        InetSocketAddress address = (InetSocketAddress) proxy.address();
        Iterator<ProxyEntity> iterator = proxyEntities.iterator();
        while (iterator.hasNext()) {
            ProxyEntity next = iterator.next();
            if (next.isExpire() || next.getIp().equals(address.getHostName())) {
                iterator.remove();
                log.info("移除代理成功---" + address.getHostName() + ":" + address.getPort());
            }
        }
    }

    public Integer getCanUsed() {
        Integer count = 0;
        if(CollectionUtils.isNotEmpty(proxyEntities)) {
            for (ProxyEntity proxyEntity : proxyEntities) {
                if(!proxyEntity.isExpire()) {
                    count++;
                }
            }
        }
        return count;
    }

}
