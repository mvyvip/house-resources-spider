package com.hs.reptilian.constant;

public interface UrlConstant {

    enum Dysj {

        GZ("http://gz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        BJ("http://bj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        NJ("http://nj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        SH("http://sh.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        SZ("http://sz.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        TJ("http://tj.01fy.cn/rent/list_2_0_0_0-0_0_0-0_0_0_0_0_0_0_2_0_1_{0}.html"),

        ;

        private String url;

        Dysj(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
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
