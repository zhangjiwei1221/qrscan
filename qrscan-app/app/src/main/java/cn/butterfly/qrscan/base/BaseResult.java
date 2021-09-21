package cn.butterfly.qrscan.base;

/**
 * 网络求返回结果
 *
 * @author zjw
 * @date 2021-09-12
 */
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

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
