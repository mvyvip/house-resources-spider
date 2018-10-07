package com.hs.reptilian.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class House {

    private String title;

    private String price;

    /**
     * 面积
     */
    private String area;

    /**
     * 小区名字
     */
    private String compoundName;

    /**
     * 小区地址
     */
    private String compoundAddress;

    /**
     * 房屋概况
     */
    private String houseSituation;

    /**
     * 所处楼层
     */
    private String floor;

    /**
     * 建筑年代
     */
    private String buildAge;

    /**
     * 产权
     */
    private String propertyRight;

    /**
     * 联系人
     */
    private String username;

    private String mobile;

    private List<String> images;

    private Date createDate;

    private Integer fromType;

    private String fromTypeDesc;

    public enum FromType {

        DI_YI_SHI_JIAN(1, "第一时间房源网");

        private int type;
        private String desc;

        FromType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

}
