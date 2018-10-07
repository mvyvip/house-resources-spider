package com.hs.reptilian.task;

import com.hs.reptilian.model.ReptilianList;
import com.hs.reptilian.service.ReptilianService;
import com.hs.reptilian.task.runnable.ReptilianRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class ReptilianTask {

    @Autowired
    private ReptilianService reptilianService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private List<ReptilianList> reptilianListList;


    private static boolean flag = false;

    @PostConstruct
    public void init() {
        this.reptilianListList = reptilianService.findAll();
    }

    /**
     * 定时爬取第一时间房源网
     */
    @Scheduled(cron = "${sync.hs.cron}")
    public void syncHsWithDysj() {
        if(flag) {
            return;
        } else {
            flag =true;
        }
        System.out.println(new Date().toLocaleString());
        Collections.shuffle(reptilianListList);
        for (ReptilianList reptilianList : reptilianListList) {
            System.out.println("reptilianList: " + reptilianList);
            taskExecutor.execute(new ReptilianRunnable(reptilianList, taskExecutor));
        }
    }

}
