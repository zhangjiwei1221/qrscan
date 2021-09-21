package cn.butterfly.qrscan.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static cn.butterfly.qrscan.constant.BaseConstants.ERROR;
import static cn.butterfly.qrscan.constant.BaseConstants.SUCCESS;

/**
 * 控制器返回结果基本类
 *
 * @author zjw
 * @date 2021-09-12
 */
@Data
@AllArgsConstructor
public class BaseResult {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    private BaseResult() {}

    /**
     * 返回成功
     *
     * @return 结果
     */
    public static BaseResult success() {
        return success(StringUtils.EMPTY);
    }

    /**
     * 返回成功
     *
     * @param data 返回数据
     * @return 结果
     */
    public static BaseResult success(Object data) {
        return success(null, data);
    }

    /**
     * 返回成功
     *
     * @param msg 成功信息
     * @param data 返回数据
     * @return 结果
     */
    public static BaseResult success(String msg, Object data) {
        return get(SUCCESS, msg, data);
    }

    /**
     * 返回错误
     *
     * @param msg 错误信息
     * @return 结果
     */
    public static BaseResult error(String msg) {
        return error(ERROR, msg);
    }

    /**
     * 返回错误
     *
     * @param code 错误码
     * @param msg 错误信息
     * @return 结果
     */
    public static BaseResult error(int code, String msg) {
        return get(code, msg, null);
    }


    /**
     * 获取返回信息实体
     *
	 * @param code 状态码
	 * @param msg 信息
	 * @param data 数据
     * @return 结果
     */
    private static BaseResult get(int code, String msg, Object data) {
        return new BaseResult(code, msg, data);
    }

}
