package com.hs.reptilian;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ProxyDemo
{
    // 代理隧道验证信息
    public final static String ProxyUser = "HNU1DM8YH0739G9D";
    public final static String ProxyPass = "649C0036B89B5BA8";

    // 代理服务器
    public final static String ProxyHost = "http-dyn.abuyun.com";
    public final static Integer ProxyPort = 9020;

    // 设置IP切换头
    public final static String ProxyHeadKey = "Proxy-Switch-Ip";
    public final static String ProxyHeadVal = "yes";

    public static Proxy proxy;

    static {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        proxy  = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static String getUrlProxyContent(String url)
    {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        try
        {
            // 此处自己处理异常、其他参数等
            Document doc = Jsoup.connect(url).timeout(3000).header(ProxyHeadKey, ProxyHeadVal).proxy(proxy).get();

            if(doc != null) {
                System.out.println(doc.body().html());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws Exception
    {

        Connection.Response execute = Jsoup.connect("http://test.abuyun.com/proxy.php")
                .timeout(20000)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("175.165.210.34", 4274)))
                .execute();
        System.out.println(execute.body());

//        Connection.Response execute = Jsoup.connect("https://www.dankegongyu.com/u/house-resource/auto-xiaoqu-name?city=%E5%B9%BF%E5%B7%9E%E5%B8%82&q=%E7%BF%A0&_=1538835368978")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                .header("Cookie", "Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1538835369; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1538831662; _ga=GA1.2.1512744423.1538832033; _gid=GA1.2.85379723.1538832033; CNZZDATA1271579284=697777463-1538826604-%7C1538832005; XSRF-TOKEN=eyJpdiI6InVzODh0M1wvQ3BpVk5hb0Q0ZEJHSTNBPT0iLCJ2YWx1ZSI6IjBzZVJWYlBjQVBhZDdiNzk2NkQ5NXRiZDZ4YU5vSCtoTXY3SW1BXC9GYmE4Nll1dTZuOXhrTEdWM3p2bGxpTk1Fc3A2UHhqZUdoS1pXamUxNzIwQ1d2Zz09IiwibWFjIjoiYzExMjY1YTgzOGIyNGI5MWZkZjExMzM2MjMwODZhMzgwNGM5ZDQyMTQyODc5OTk0YjdkNGE5MDcxNDUwOTc2NyJ9; session=eyJpdiI6IkwwaGNnVHUxcTJPXC9jVXo0NGNoclh3PT0iLCJ2YWx1ZSI6ImV1Z0w3RHpPaEJjRWtCUXpmaEU5ZGFtVDB3Z1E0TkVqdm1VMFwvaEc3bXVBbWNhaHY1bktraVFnUktcL1llcWx1NEdraTg4SFYxNXFzNDFcL1d2cnltOFh3PT0iLCJtYWMiOiI3MjU0YzIxYmZlNWZjNjY2MzY0Yzg3YTMyYjc3MWNiNTE5YzVlZDNkYjI2ODU3MjhhYjM3MjRjNjBlMGFmZjBmIn0%3D; _gat=1; externalHouseRecorderPassengerClose=true; UM_distinctid=1664983bc20113-02217f114ca7ee-664b2f6c-3d10d-1664983bc214aa; TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
//                .header("X-Requested-With", "XMLHttpRequest")
//                .header("X-Tingyun-Id", "HfvUoHu3rrU;r=835988035")
//                .header("X-CSRF-TOKEN", "KY4pkm2KZhx7DBNhpURSV3oaLGqD8uYFTrXXz1hx")
//                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .ignoreContentType(true)
//                .execute();
//        System.out.println(execute.body());


//        Connection.Response response = Jsoup.connect("https://www.dankegongyu.com/u")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
////                .header("Cookie", "XSRF-TOKEN=eyJpdiI6IkJ4Q3Q5eXZhRFo1STFxaXFZZUJFUEE9PSIsInZhbHVlIjoiME5YOU14REMyMllpcHZrK1BqVnJWczV6bkduVE4yRDIrWkl3NU15d1VuUUcxU3hqNm80NFo1c2Yyc3lKMVZOVlhQUEd6VHdtMWVBK1UrakRUYjNnUXc9PSIsIm1hYyI6IjIzYjA5ZGFiYTE3Y2Q4NWVlY2YzMDcxZTQ5NjYxNGU5OTNjMDM4YWM5ZmI4MzI2MDY2YTFjYWZhYWMxOTI3NGIifQ%3D%3D; session=eyJpdiI6IlhYdVdxTnZORkZCRW9Ubm02dmViRlE9PSIsInZhbHVlIjoiMlhJQ0dVbHJnekZoZXlnZE1jemxxR3poRjZEZjl5bENvd04yWDVKV3hxNjVLS0NvaHEzXC9ZbjZ4cmRYXC9pdGIxeExUZU1pU0E5N2h5NFwvellndUNLMXc9PSIsIm1hYyI6IjQ3Y2RmZjc3YTE1OTg5NDNmYjBjYmViZWJmZTA5ZmI0OTQzZGMyYmI1YTU4NTI0YjE2YWRhYzYzNTE5MjY0NTkifQ%3D%3D; Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1538832442; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1538831662; _ga=GA1.2.1512744423.1538832033; _gid=GA1.2.85379723.1538832033; CNZZDATA1271579284=697777463-1538826604-%7C1538832005; externalHouseRecorderPassengerClose=true; UM_distinctid=1664983bc20113-02217f114ca7ee-664b2f6c-3d10d-1664983bc214aa; TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
//                .header("Cookie", "XSRF-TOKEN=eyJpdiI6IkJ4Q3Q5eXZhRFo1STFxaXFZZUJFUEE9PSIsInZhbHVlIjoiME5YOU14REMyMllpcHZrK1BqVnJWczV6bkduVE4yRDIrWkl3NU15d1VuUUcxU3hqNm80NFo1c2Yyc3lKMVZOVlhQUEd6VHdtMWVBK1UrakRUYjNnUXc9PSIsIm1hYyI6IjIzYjA5ZGFiYTE3Y2Q4NWVlY2YzMDcxZTQ5NjYxNGU5OTNjMDM4YWM5ZmI4MzI2MDY2YTFjYWZhYWMxOTI3NGIifQ%3D%3D; session=eyJpdiI6IlhYdVdxTnZORkZCRW9Ubm02dmViRlE9PSIsInZhbHVlIjoiMlhJQ0dVbHJnekZoZXlnZE1jemxxR3poRjZEZjl5bENvd04yWDVKV3hxNjVLS0NvaHEzXC9ZbjZ4cmRYXC9pdGIxeExUZU1pU0E5N2h5NFwvellndUNLMXc9PSIsIm1hYyI6IjQ3Y2RmZjc3YTE1OTg5NDNmYjBjYmViZWJmZTA5ZmI0OTQzZGMyYmI1YTU4NTI0YjE2YWRhYzYzNTE5MjY0NTkifQ%3D%3D; Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1538832442; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1538831662; _ga=GA1.2.1512744423.1538832033; _gid=GA1.2.85379723.1538832033; CNZZDATA1271579284=697777463-1538826604-%7C1538832005; externalHouseRecorderPassengerClose=true; UM_distinctid=1664983bc20113-02217f114ca7ee-664b2f6c-3d10d-1664983bc214aa; TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
//                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
//                .execute();
//        System.out.println(response.body());
//
//        Map<String, String> cookies = response.cookies();
//        System.out.println(cookies);
//
//        Connection.Response execute = Jsoup.connect("https://www.dankegongyu.com/u/house-resource/insert")
//                .method(Connection.Method.POST)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
////                .header("Cookie", "XSRF-TOKEN=eyJpdiI6IkJ4Q3Q5eXZhRFo1STFxaXFZZUJFUEE9PSIsInZhbHVlIjoiME5YOU14REMyMllpcHZrK1BqVnJWczV6bkduVE4yRDIrWkl3NU15d1VuUUcxU3hqNm80NFo1c2Yyc3lKMVZOVlhQUEd6VHdtMWVBK1UrakRUYjNnUXc9PSIsIm1hYyI6IjIzYjA5ZGFiYTE3Y2Q4NWVlY2YzMDcxZTQ5NjYxNGU5OTNjMDM4YWM5ZmI4MzI2MDY2YTFjYWZhYWMxOTI3NGIifQ%3D%3D; session=eyJpdiI6IlhYdVdxTnZORkZCRW9Ubm02dmViRlE9PSIsInZhbHVlIjoiMlhJQ0dVbHJnekZoZXlnZE1jemxxR3poRjZEZjl5bENvd04yWDVKV3hxNjVLS0NvaHEzXC9ZbjZ4cmRYXC9pdGIxeExUZU1pU0E5N2h5NFwvellndUNLMXc9PSIsIm1hYyI6IjQ3Y2RmZjc3YTE1OTg5NDNmYjBjYmViZWJmZTA5ZmI0OTQzZGMyYmI1YTU4NTI0YjE2YWRhYzYzNTE5MjY0NTkifQ%3D%3D; Hm_lpvt_814ef98ed9fc41dfe57d70d8a496561d=1538832442; Hm_lvt_814ef98ed9fc41dfe57d70d8a496561d=1538831662; _ga=GA1.2.1512744423.1538832033; _gid=GA1.2.85379723.1538832033; CNZZDATA1271579284=697777463-1538826604-%7C1538832005; externalHouseRecorderPassengerClose=true; UM_distinctid=1664983bc20113-02217f114ca7ee-664b2f6c-3d10d-1664983bc214aa; TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
////                .header("Cookie", "XSRF-TOKEN=eyJpdiI6IlVvalwvRXZvcCtYWExFNHZaWHd0Mkd3PT0iLCJ2YWx1ZSI6ImVEZEdPM3Mwc1V3aDZZSVA0Zm9GXC8yRkVEWkJsZjRUWkFkanNrdTFpUmVjMVJmejRzeFQ0Ukx0dGxGWmZ6UWNPY0N0USsxRzZ4V21QK01xMjlpZ0RjQT09IiwibWFjIjoiZGM4ZDAxYWRhZWJmZmZkZmIxYTJkNDNkMDc2NjcyOTBkY2E3YjMwZjI4ZjNkMzdhMGVhZTVkMjcxZDMxNTY5YyJ9; " +
////                        "session=eyJpdiI6InkyVUdSTTlaUzBhUGxrWFJ2T0VTMmc9PSIsInZhbHVlIjoid0dFRkprYkp4U2FBN09wQ2xIeW5hc3lTUGJvWUFhTjdPSjdcL0Flczg2bXEzUUVqNHU4aGNlU01UcWxSM1Y2OXJyeWVEY1dIenBGT2FzXC9qQ1lJXC9Ra3c9PSIsIm1hYyI6IjkxMmNmMzM2NWM2Mjc3MDRkMjgxYzAyODc5Nzc1OTg1MWFiMDc3NmY4ZTczY2MzODQwOTU2ZjNhMDMyMDM0NzcifQ%3D%3D; " +
////                        "TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
//                .header("Cookie", "XSRF-TOKEN=eyJpdiI6Ik9BYzZcL3E1dndQdG1XQVZYQ043ZENRPT0iLCJ2YWx1ZSI6ImNvZTN1bnNjZ1IzOWhaWUJUUVExS242anFTZ3IwNVNhWTB3cUNcL05nM2FENzNFNG9Pb2NUMEEyMnlYTFdpcTdTc1VFR2MyRzRTSDBcL3hYSHhSOUFoXC9RPT0iLCJtYWMiOiIyNjVmYjU3ZDlmYzg0NjMyZmRiZTI0ODdhYzE1MDhjOWRlZmU2YTQ1Nzg2M2QxMDQyMjBhZDE3Y2YxYWQxYjYwIn0%3D; session=eyJpdiI6IlZcL3ZlejBmcW9FSVZ2YU5YbVVrTmp3PT0iLCJ2YWx1ZSI6IlVOODQwY3pYcFh5c0Z3N3YrMlhrOXhvWVB5dkRYRTVQVktxSFQ1aHFNRGFvK1Q5RE9LQnR3WlBCZytcLzU1Y0s2UEVaNmJLV3V0dlFkb1pEUkhLV2Fidz09IiwibWFjIjoiM2Q1YTViNWE3OWJkYWJkNGU4MDZlMjhiNTQ2NDI4MDI1ZGM4MTgxMmRhYzRiZjQ0OWU1M2FjNjA2NGEwNmI2ZCJ9; TY_SESSION_ID=f541f901-e0a1-493d-a66c-f6bd3bdf463f")
//                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN")
//                .ignoreContentType(true)
////                .header("X-Requested-With", "XMLHttpRequest")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .data("_token", "KY4pkm2KZhx7DBNhpURSV3oaLGqD8uYFTrXXz1hx")
//                .data("city", "广州市")
//                .data("doorplate", "1")
//                .data("landlord_name", "测试不用管")
//                .data("landlord_phone", "13984533168")
//                .data("xiaoqu_id", "47586")
//                .execute();
//
//        System.err.println(execute.body());
    /*    // 要访问的目标页面
        String targetUrl = "http://test.abuyun.com/proxy.php";
        //String targetUrl = "http://proxy.abuyun.com/switch-ip";
        //String targetUrl = "http://proxy.abuyun.com/current-ip";

        getUrlProxyContent(targetUrl);*/
    }
}
