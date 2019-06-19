package com.fly.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtil {


    /**
     * 拼接后的字符串
     *
     * @param obj
     * @return
     */
    public static String append(Object... obj) {
        StringBuffer mStringBuffer = new StringBuffer();

        for (Object anObj : obj) {
            mStringBuffer.append(anObj);
        }
        return mStringBuffer.toString();
    }


    /**
     * 替换字符串
     *
     * @param from
     * @param to
     * @param source
     * @return
     */
    public static String replace(String from, String to, String source) {
        if (from == null || to == null || source == null) {
            return null;
        } else {
            return source.replaceAll(from, to);
        }
    }


    /**
     * 检查手机号
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkMobile(String phoneNum) {
        Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }


    /**
     * 验证身份证号码
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        if (idCard.length() != 15 && idCard.length() != 18) {
            return false;
        }
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }


    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Matcher m = Pattern.compile(regex).matcher(email);
        if (m.matches()) {
            return true;
        } else
            return false;
    }


    /**
     * 验证字符串合法性
     *
     * @param str
     * @param rex
     * @return
     */
    public static boolean checkLegal(String str, String rex) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }
        boolean flag = false;
        for (int i = 0; i < rex.length(); i++) {
            if (str.indexOf(rex.charAt(i)) != -1) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    /**
     * 断list中是否包含某一个字符串
     *
     * @param list
     * @param str
     * @return
     */
    public static boolean listContain(List<String> list, String str) {
        return !CollectionUtils.isEmpty(list) && list.contains(str);
    }


    /**
     * 去除转义
     *
     * @param text
     * @return
     */
    public static String escapeString(String text) {
        if (!StringUtils.isEmpty(text)) {
            text = text.replaceAll("[\\n\\r]*", "");
        }
        return text;
    }


    /**
     * 转码中文字符串
     *
     * @param srcStr
     * @return
     */
    public static String encodeChineseStr(String srcStr) throws UnsupportedEncodingException {
        String dstStr = srcStr;
        if (!StringUtils.isEmpty(dstStr) && srcStr.length() < srcStr.getBytes().length) {
            dstStr = URLEncoder.encode(dstStr, "utf-8");
        }
        return dstStr;
    }


    /**
     * 截取指定长度 从0开始，包左不包右
     *
     * @param dateStr
     * @param start
     * @param end
     * @return
     */
    public static String spliteTime(String dateStr, int start, int end) {
        CharSequence sequence = dateStr.subSequence(start, end);
        return sequence.toString();
    }

    /**
     * 是否有中文字符
     *
     * @param str
     * @return
     */
    public static boolean hasChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }


}
