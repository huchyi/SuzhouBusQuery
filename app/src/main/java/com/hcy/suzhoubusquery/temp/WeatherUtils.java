package com.hcy.suzhoubusquery.temp;

import com.hcy.suzhoubusquery.R;

/**
 * Created by hcy on 2016/1/28.
 * <p/>
 * WeatherUtils
 */
public class WeatherUtils {


    /**
     * 根据天气的类型名称返回对应的图片资源id类型值
     *
     * @param name 天气类型名称
     * @return 对应的int类型值，0表示位置类型
     */
    public static int getWhiteIconIdByTypeName(String name) {
        name = nameReset(name);
        int resId = 0;
        if (name.equals("晴")) {
            resId = R.drawable.ic_sun;
        } else if (name.equals("多云")) {
            resId = R.drawable.ic_cloudy;
        } else if (name.equals("阴")) {
            resId = R.drawable.ic_overcast;
        } else if (name.equals("雾")) {
            resId = R.drawable.ic_fog;
        } else if (name.equals("雨夹雪")) {
            resId = R.drawable.ic_yujiaxue;
        } else if (name.equals("小雨")) {
            resId = R.drawable.ic_xiaoyu;
        } else if (name.equals("小雪")) {
            resId = R.drawable.ic_xiaoxue;
        } else if (name.equals("中雨")) {
            resId = R.drawable.ic_zhongyu;
        } else if (name.equals("阵雨")) {
            resId = R.drawable.ic_leizhenyu;
        } else if (name.equals("阵雪")) {
            resId = R.drawable.ic_zhenxue;
        } else if (name.equals("中雪")) {
            resId = R.drawable.ic_zhongxue;
        } else if (name.equals("大雪")) {
            resId = R.drawable.ic_daxue;
        } else if (name.equals("暴雪")) {
            resId = R.drawable.ic_baoxue;
        } else if (name.equals("大雨")) {
            resId = R.drawable.ic_dayu;
        } else if (name.equals("暴雨")) {
            resId = R.drawable.ic_baoyu;
        }else if (name.equals("霾")) {
            resId = R.drawable.ic_mai;
        }
//        else if (name.contains("中雪")) {
//            resId = R.drawable.ic_zhongxue;
//        } else if (name.contains("中雨")) {
//            resId = R.drawable.ic_zhongyu;
//        } else if (name.contains("大雨")) {
//            resId = R.drawable.ic_dayu;
//        } else if (name.contains("暴雨")) {
//            resId = R.drawable.ic_baoyu;
//        } else if (name.contains("暴雪")) {
//            resId = R.drawable.ic_baoxue;
//        } else if (name.contains("大雪")) {
//            resId = R.drawable.ic_daxue;
//        }

        return resId;
    }

    /**
     * 根据天气的类型名称返回对应的图片资源id类型值
     *
     * @param name 天气类型名称
     * @return 对应的int类型值，0表示位置类型
     */
    public static int getBlackIconIdByTypeName(String name) {
        name = nameReset(name);
        int resId = 0;
        if (name.equals("晴")) {
            resId = R.drawable.ic_sun_black;
        } else if (name.equals("多云")) {
            resId = R.drawable.ic_cloudy_black;
        } else if (name.equals("阴")) {
            resId = R.drawable.ic_overcast_black;
        } else if (name.equals("雾")) {
            resId = R.drawable.ic_fog_black;
        } else if (name.equals("雨夹雪")) {
            resId = R.drawable.ic_yujiaxue_black;
        } else if (name.equals("小雨")) {
            resId = R.drawable.ic_xiaoyu_black;
        } else if (name.equals("小雪")) {
            resId = R.drawable.ic_xiaoxue_black;
        } else if (name.equals("中雨")) {
            resId = R.drawable.ic_zhongyu_black;
        } else if (name.equals("阵雨")) {
            resId = R.drawable.ic_leizhenyu_black;
        } else if (name.equals("阵雪")) {
            resId = R.drawable.ic_zhenxue_black;
        } else if (name.equals("中雪")) {
            resId = R.drawable.ic_zhongxue_black;
        } else if (name.equals("大雪")) {
            resId = R.drawable.ic_daxue_black;
        } else if (name.equals("暴雪")) {
            resId = R.drawable.ic_baoxue_black;
        } else if (name.equals("大雨")) {
            resId = R.drawable.ic_dayu_black;
        } else if (name.equals("暴雨")) {
            resId = R.drawable.ic_baoyu_black;
        } else if (name.contains("中雪")) {
            resId = R.drawable.ic_zhongxue_black;
        } else if (name.contains("中雨")) {
            resId = R.drawable.ic_zhongyu_black;
        } else if (name.contains("大雨")) {
            resId = R.drawable.ic_dayu_black;
        } else if (name.contains("暴雨")) {
            resId = R.drawable.ic_baoyu_black;
        } else if (name.contains("暴雪")) {
            resId = R.drawable.ic_baoxue_black;
        } else if (name.contains("大雪")) {
            resId = R.drawable.ic_daxue_black;
        } else if (name.equals("霾")) {
            resId = R.drawable.ic_mai_black;
        }
        return resId;
    }


    public static int getWhiteCatIconIdByTypeName(String name) {
        name = nameReset(name);
        int resId = 0;
        if (name.equals("晴")) {
            resId = R.drawable.ic_sun_cat;
        } else if (name.equals("多云")) {
            resId = R.drawable.ic_cloudy_cat;
        } else if (name.equals("阴")) {
            resId = R.drawable.ic_overcast_cat;
        } else if (name.equals("雾")) {
            resId = R.drawable.ic_fog_cat;
        } else if (name.equals("雨夹雪")) {
            resId = R.drawable.ic_yujiaxue_cat;
        } else if (name.equals("小雨")) {
            resId = R.drawable.ic_xiaoyu_cat;
        } else if (name.equals("小雪")) {
            resId = R.drawable.ic_xiaoxue_cat;
        } else if (name.equals("中雨")) {
            resId = R.drawable.ic_zhongyu_cat;
        } else if (name.equals("阵雨")) {
            resId = R.drawable.ic_leizhenyu_cat;
        } else if (name.equals("大雨")) {
            resId = R.drawable.ic_dayu_cat;
        } else if (name.equals("暴雨")) {
            resId = R.drawable.ic_baoyu_cat;
        } else if (name.equals("阵雪")) {
            resId = R.drawable.ic_zhongxue_cat;
        } else if (name.equals("中雪")) {
            resId = R.drawable.ic_zhongxue_cat;
        } else if (name.equals("大雪")) {
            resId = R.drawable.ic_daxue_cat;
        } else if (name.equals("暴雪")) {
            resId = R.drawable.ic_baoxue_cat;
        }else if (name.equals("霾")) {
            resId = R.drawable.ic_mai_cat;
        }
        return resId;
    }


    private static String nameReset(String name) {
        if (name.length() <= 2) {
            return name;
        } else if (name.length() > 3) {
            String name1 = name.substring(name.length() - 3, name.length());
            String name2 = name.substring(name.length() - 2, name.length());
            if (name1.contains("转") && name2.contains("转")) {
                return name.substring(name.length() - 1, name.length());
            } else if (name1.contains("转") && !name2.contains("转")) {
                return name2;
            }else if (!name1.contains("转") && !name2.contains("转")) {
                return name1;
            }
        }
        return name;
    }

}
