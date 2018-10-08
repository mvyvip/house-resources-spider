package com.hs.reptilian.model;


import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class ProxyEntity {

    public ProxyEntity(String ip, Integer port, Date expireTime) {
        this.ip = ip;
        this.port = port;
        this.expireTime = expireTime;
    }

    private String ip;

    private Integer port;

    private Date expireTime;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isExpire() {
        return System.currentTimeMillis() > DateUtils.addSeconds(expireTime, -1).getTime();
    }

    @Override
    public String toString() {
        return "ProxyEntity{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", expireTime=" + expireTime.toLocaleString() +
                '}';
    }
}
