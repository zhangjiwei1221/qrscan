package cn.butterfly.qrscan.vo;

import cn.butterfly.qrscan.base.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 二维码值对象
 *
 * @author zjw
 * @date 2021-09-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeVO {

    /**
     * 二维码状态
     */
    private CodeStatus codeStatus;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    public CodeVO(CodeStatus codeStatus) {
        this.codeStatus = codeStatus;
    }

}
