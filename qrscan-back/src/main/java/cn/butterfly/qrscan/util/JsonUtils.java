package cn.butterfly.qrscan.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static cn.butterfly.qrscan.constant.BaseConstants.DATE_TIME_FORMAT_PATTERN;

/**
 * JSON 操作工具类
 *
 * @author zjw
 * @date 2021-09-14
 */
public class JsonUtils {

    private JsonUtils() {}

    private static final Gson JSON = new GsonBuilder().setDateFormat(DATE_TIME_FORMAT_PATTERN).create();

    /**
     * 将指定对象转为 JSON 字符串
     *
	 * @param obj 对象
     * @return JSON 字符串
     */
    public static String stringify(Object obj) {
        return JSON.toJson(obj);
    }

    /**
     * 将 JSON 字符串转为指定的类型
     *
	 * @param jsonString JSON 字符串
	 * @param typeOfT 要转换的类型
     * @return 转换后的内容
     */
    public static <T> T parse(String jsonString, Class<T> typeOfT) {
        return JSON.fromJson(jsonString, typeOfT);
    }

}
