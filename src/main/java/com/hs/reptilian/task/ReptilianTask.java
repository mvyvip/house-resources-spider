package com.hs.reptilian.task;

import com.hs.reptilian.constant.UrlConstant;
import com.hs.reptilian.model.ReptilianList;
import com.hs.reptilian.service.ReptilianService;
import com.hs.reptilian.task.runnable.ReptilianRunnable;
import com.hs.reptilian.util.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Scheduled(cron = "${sync.hs.cron}")
    public void syncHsWithDysj() {
        log.info("开始爬取第一时间数据");
        for (UrlConstant.Dysj dysj : UrlConstant.Dysj.values()) {
            taskExecutor.execute(new ReptilianRunnable(dysj, proxyUtil));
        }
    }

}
