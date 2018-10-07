package com.hs.reptilian.controller;

import com.hs.reptilian.ProxyDemo;
import com.hs.reptilian.mapper.TestMapper;
import com.hs.reptilian.model.House;
import com.hs.reptilian.util.ViewData;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class HsController {

    @Autowired
    private TestMapper testMapper;

    @GetMapping("/test")
    public ViewData test2() throws Exception {
        List<Map<String, Object>> allOrder = testMapper.findAllOrder();

        allOrder.stream().forEach(map -> {
            String userId = map.get("cm_user_id").toString();
            BigDecimal count = new BigDecimal(map.get("count").toString());
            System.out.println(userId + " - " + count);
            testMapper.update(userId, count);
        });

//        testMapper.update("2547", new BigDecimal(8700));

//        testMapper.update("22692", new BigDecimal(1000));


        return ViewData.builder().build();
    }

    @GetMapping("/list")
    public ViewData list() throws Exception {
        List<House> houses = new ArrayList<>();
        Connection.Response response = Jsoup.connect("http://bj.01fy.cn/sale/list_2_0_0_0-0_0_0-0_0_0_0-0_0_0-0_2_0_1_.html")
                .header(ProxyDemo.ProxyHeadKey, ProxyDemo.ProxyHeadVal).proxy(ProxyDemo.proxy)
                .timeout(20000)
                .execute();

        System.out.println(response.body());
        Elements elements = response.parse().getElementById("list").getElementsByTag("li");
        for (Element element : elements) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       House house = getHouse(element.getElementsByTag("a").attr("href"));
                       if(house != null) {
                           houses.add(house);
                       }
                   } catch (Exception e) {
                       System.err.println(e.getMessage());
                   }
               }
           }).start();
        }
        Thread.sleep(10000);
        return ViewData.builder().data(houses).total(houses.size()).build();
    }

    @GetMapping("/refresh")
    public ViewData test() throws IOException {
        List<House> houses = new ArrayList<>();


        return ViewData.builder().data(houses).total(1).build();
    }

    private House getHouse(String url) throws Exception {
        Connection.Response response = Jsoup.connect("http://bj.01fy.cn/sale/" + url)
                .header(ProxyDemo.ProxyHeadKey, ProxyDemo.ProxyHeadVal).proxy(ProxyDemo.proxy)
                .timeout(20000)
                .execute();
//        System.out.println(response.body());
        if(response.body().contains("请您输入验证码")) {
            return null;
        }
        Document document = Jsoup.parse(response.body());


        House house = new House();
        Elements images = document.getElementsByClass("thumb-list");
        System.err.println(images);
        if(CollectionUtils.isNotEmpty(images)) {
            Elements img = images.get(0).getElementsByTag("img");
            List<String> hsImages = new ArrayList<>();
            for (Element i : img) {
                hsImages.add(i.attr("big-pic"));
            }
            house.setImages(hsImages);
        }

        Elements tables = document.getElementsByClass("cr_left");
        Elements dd = tables.get(0).getElementsByTag("dd");
        house.setPrice(dd.get(0).text());
        house.setArea(dd.get(1).text());
        house.setCompoundName(dd.get(2).text());
        house.setCompoundAddress(dd.get(3).text());
        house.setHouseSituation(dd.get(4).text());
        house.setFloor(dd.get(5).text());
        house.setBuildAge(dd.get(6).text());
        house.setPropertyRight(dd.get(7).text());
        house.setUsername(dd.get(8).text());
        house.setMobile(dd.get(9).text().replaceAll( " ", "-"));
//        house.setTitle(document.getElementsByTag("h1").text());
        house.setTitle("<a href='#'>" + document.getElementsByTag("h1").text() + "</a>");
        house.setCreateDate(new Date());
        house.setFromType(House.FromType.DI_YI_SHI_JIAN.getType());
        house.setFromTypeDesc(House.FromType.DI_YI_SHI_JIAN.getDesc());
        return house;
    }

}
