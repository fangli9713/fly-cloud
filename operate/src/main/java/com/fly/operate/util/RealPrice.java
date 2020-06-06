package com.fly.operate.util;


import com.fly.common.util.http.HttpUtil;

/**
 * Created by Fangln on 2018/11/7.
 */
public class RealPrice {

//    final private static String BASE_URL = "https://api.shenjian.io/?appid=1fc26749eefff6ef0bdd3cb0602300b4";
    final private static String BASE_URL = "http://hq.sinajs.cn/list=";

    public static void main(String[] args) {

        real("sz300468");
    }

    public static void real(String code){

        StringBuilder builder = new StringBuilder(BASE_URL).append(code);
        try {
            final String s = HttpUtil.doRequest(builder.toString(), null, null);
            String ss = new String(s.getBytes(),"gbk");
            System.out.println(ss);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
