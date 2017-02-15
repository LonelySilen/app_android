package silen.app.utils;

import java.util.ArrayList;

/*
 *
 *
 * 版 权 :@Copyright *****有限公司版权所有
 *
 * 作 者 :lonelysilen
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/11/10  10:21
 *
 * 描 述 :*****处理类
 *
 * 修订日期 :
 */

/**
 * Created by admin on 2016/10/21.
 */

public class FormatArry {

    private static String url = "http://192.168.1.118:10081/IndexHandler.ashx?";    //地址
    private static String cmd_h = "b=";                 //命令字
    private static String separator ="@`";              //分隔符

    public static String formatArryToUrl(String cmd_num, ArrayList<String> str){
        StringBuffer sb = new StringBuffer(url);
        sb.append(cmd_h).append(cmd_num);
        for(int i = 0; i < str.size(); i++){
            sb.append(separator);
            sb.append(str.get(i));
        }
        String result = sb.toString();
        return result;
    }
    public static String formatArryToParam(ArrayList<String> str){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.size(); i++){
            sb.append(separator);
            sb.append(str.get(i));
        }
        String result = sb.toString();
        return result;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        FormatArry.url = "http://"+url+"/IndexHandler.ashx?";
    }
}
