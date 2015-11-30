package com.hcy.suzhoubusquery.utils;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 判断字符创的内容是否为空
     *
     * @param str
     * @return true,字符串为null 或 ""
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }

    /**
     * 秒数转为 mm:ss的格式
     *
     * @param second 秒
     * @return mm:ss
     */
    public static String formatss2mmColomss(int second) {
        return second > 0 && second < 3600 ? String.format("%02d:%02d", second / 60, second % 60) : "00:00";
    }

    public static String cleanNull(String src) {
        if (StringUtils.isEmpty(src) || "null".equals(src.toLowerCase().trim())) {
            return "";
        }
        return src;
    }

    public static boolean isNullOrNullStr(String str) {
        return null == str || "".equals(str.trim()) || "null".equals(str.toLowerCase().trim());
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str 需要判断的字符串
     * @return true:是字符串；false:不是字符串
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断字符串是否为正整数或正小数
     *
     * @param str 需要判断的字符串
     * @return true:是字符串；false:不是字符串
     */
    public static boolean isNumOrFloat(String str) {
        Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

//    public static String getFirstSpell(String chinese) {
//        StringBuffer pybf = new StringBuffer();
//        try {
//            char[] arr = chinese.toCharArray();
//            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//            defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//            for (int i = 0; i < arr.length; i++) {
//                if (arr[i] > 128) {
//                    try {
//                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
//                        if (temp != null) {
//                            pybf.append(temp[0].charAt(0));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    pybf.append(arr[i]);
//                }
//            }
//        } catch (Exception e) {}
//        return pybf.toString().trim().replaceAll("\\W", "");
//    }

    public static String getTimeErgonHalfhour() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean flag = minute / 30 == 0;
        String eventTimeLabel = "";
        if (flag) {
            eventTimeLabel = hour + ":00" + "~" + hour + ":30";
        } else {
            eventTimeLabel = hour + ":30" + "~" + (hour + 1) + ":00";
        }
        return eventTimeLabel;
    }

    public static char bin2Char(String binContent) {
        return (char) (Integer.parseInt((new BigInteger(binContent, 2).toString(10))));
    }

    public static String rightPad(String s, int length, char c) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = length - s.length(); i > 0; i--)
            sb.append(c);
        return sb.toString();
    }

    public static String leftPad(String s, int length, char c) {
        StringBuffer sb = new StringBuffer();
        for (int i = length - s.length(); i > 0; i--)
            sb.append(c);
        sb.append(s);
        return sb.toString();
    }

    /**
     * 是否包括中文
     */
    public static boolean isHasChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 判断是否为有效电话号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidOfPhoneNumber(String phoneNumber) {
        boolean isValid = false;
        /*
         * 可接受的电话格式有：
         */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        /*
         * 可接受的电话格式有：
         */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断是否是一个IP
     *
     * @param IP
     * @return
     */
    public static boolean isIp(String IP) {
        boolean b = false;
        IP = IP.trim();
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255) if (Integer.parseInt(s[1]) < 255) if (Integer.parseInt(s[2]) < 255) if (Integer.parseInt(s[3]) < 255) b = true;
        }
        return b;
    }

}
