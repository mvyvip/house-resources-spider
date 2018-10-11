package com.hs.reptilian.constant;

public interface UrlConstant {

    enum FangTianXia {

         GZ("http://gz.zu.fang.com/house/a21-h316-i32-n31/", "广州市"),

//        SH("http://sh.zu.fang.com/house/a21-h31-i32-n31/", "上海市"),

//        BJ("http://zu.fang.com/house/a21-h31-n31/", "北京市"),

         SZ("http://sz.zu.fang.com/house/a21-h316-n31/", "深圳市"),

         TJ("http://tj.zu.fang.com/house/a21-h316-n31/", "天津市"),

         NJ("http://nanjing.zu.fang.com/house/a21-h316-n31/", "南京市"),

        ;


        private String url;

        private String city;

        FangTianXia(String url, String city) {
            this.url = url;
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public String getCityPrefix() {
            // TODO 北京需要特殊处理
            return url.split("//")[1].split("\\.")[0];
        }

        public String getUrl() {
            return url;
        }
    }

    enum Dysj {

        GZ("http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "广州市"),

        BJ("http://bj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "北京市"),

        NJ("http://nj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "南京市"),

        SH("http://sh.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "上海市"),

        SZ("http://sz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "深圳市"),

        TJ("http://tj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_.html", "天津市"),

        ;

        private String url;

        private String city;

        Dysj(String url, String city) {
            this.url = url;
            this.city = city;
        }

        public String getUrl() {
            return url;
        }

        public String getCity() {
            return city;
        }

        public static String getUrl(String city) {
            if("广州市".equals(city)) {
                return GZ.url;
            } else if("上海市".equals(city)) {
                return SH.url;
            } else if("上海市".equals(city)) {
                return SH.url;
            } else if("深圳市".equals(city)) {
                return SZ.url;
            } else if("南京市".equals(city)) {
                return  NJ.url;
            } else if("天津市".equals(city)) {
                return TJ.url;
            } else if("北京市".equals(city)) {
                return BJ.url;
            }
            throw new RuntimeException(city + "未录入");
        }

        //
//
//        /**
//         * 第一时间北京个人租房
//         */
//        String DYSJ_GZ = "http://bj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html";
//
//        /**
//         * 第一时间南京个人租房
//         */
//        String DYSJ_GZ = "http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html";
//
//        /**
//         * 第一时间上海个人租房
//         */
//        String DYSJ_GZ = "http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html";
//
//        /**
//         * 第一时间深圳个人租房
//         */
//        String DYSJ_GZ = "http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html";
//
//        /**
//         * 第一时间天津个人租房
//         */
//        String DYSJ_GZ = "http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html";
    }


}
