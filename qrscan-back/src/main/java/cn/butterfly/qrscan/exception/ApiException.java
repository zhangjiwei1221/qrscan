package cn.butterfly.qrscan.exception;


/**
 * 全局全局异常类
 *
 * @author zjw
 * @date 2020-10-28
 */
public class ApiException extends RuntimeException {

	public ApiException(String message) {
		super(message);
	}

}
