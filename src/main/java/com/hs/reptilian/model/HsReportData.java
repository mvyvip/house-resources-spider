package com.hs.reptilian.model;

import lombok.Data;

@Data
public class HsReportData {

    private String city;

    /***
     * 门牌
     */
    private String card;

    private String mobile;

    private String username;

    /**
     * 小区名字
     */
    private String compoundName;

}
