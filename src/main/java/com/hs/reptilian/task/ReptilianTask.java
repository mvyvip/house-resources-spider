package com.hs.reptilian.task;

import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.service.ReptilianService;
import com.hs.reptilian.task.runnable.DysjSpiderRunnable;
import com.hs.reptilian.task.runnable.FtxSpiderRunnable;
import com.hs.reptilian.task.runnable.GanjiRunnable;
import com.hs.reptilian.util.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ReptilianTask {

    @Autowired
    private ReptilianService reptilianService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ProxyUtil proxyUtil;

    /**
     * 定时爬取第一时间房源网
     */
    @Async
//    @Scheduled(cron = "${sync.hs.cron}")
    public void syncHsWithDysj() {
        log.info("开始爬取第一时间数据" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (UrlConstant.Dysj dysj : UrlConstant.Dysj.values()) {
                    taskExecutor.execute(new DysjSpiderRunnable(dysj, proxyUtil));
                }
            }
        }).start();
        log.info("结束爬取第一时间数据" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---end");
    }

    /**
     * 定时爬取房天下数据
     */
    @Async
//    @Scheduled(cron = "${sync.hs.cron}")
    public void syncHsWithFtx() {
        log.info("开始爬取房天下" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (UrlConstant.FangTianXia ftx : UrlConstant.FangTianXia.values()) {
                    taskExecutor.execute(new FtxSpiderRunnable(ftx, proxyUtil));
                }
            }
        }).start();
        log.info("结束爬取第一时间数据" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---end");
    }

    /**
     * 定时爬取赶集网
     */
    @Async
    @Scheduled(cron = "${sync.hs.gjw}")
    public void syncHsWithGanji() {
        log.info("开始爬取赶集网" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (UrlConstant.GanJiWang gjw : UrlConstant.GanJiWang.values()) {
                    taskExecutor.execute(new GanjiRunnable(gjw, proxyUtil));
                }
            }
        }).start();
        log.info("结束爬取赶集网数据" + new Date().toLocaleString() + "---" + Thread.currentThread().getName() + "---end");
    }

}
