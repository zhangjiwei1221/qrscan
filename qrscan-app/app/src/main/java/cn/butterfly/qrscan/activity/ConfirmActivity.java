package cn.butterfly.qrscan.activity;

import static cn.butterfly.qrscan.base.BaseConstants.API;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.XUI;
import com.yutils.http.YHttp;
import com.yutils.http.contract.YHttpListener;

import java.util.Map;

import cn.butterfly.qrscan.R;
import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.util.JsonUtils;

/**
 * 确认登录界面
 *
 * @author zjw
 * @date 2021-09-20
 */
public class ConfirmActivity extends AppCompatActivity {

    /**
     * 临时凭证
     */
    private String tmpToken;

    /**
     * 二维码
     */
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        TextView loginInfo = findViewById(R.id.loginInfo);
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String browser = intent.getStringExtra("browser");
        String os = intent.getStringExtra("os");
        loginInfo.setText(address + "\t" + browser + "\t" + os);
        tmpToken = intent.getStringExtra("token");
        code = intent.getStringExtra("code");
    }


    /**
     * 确认登录处理
     *
     * @param view 确认登录按钮组件
     */
    public void confirm(View view) {
        String url = API + "/code/scan?code=" + code + "&token=" + tmpToken;
        YHttp.create().get(url, new YHttpListener() {
            @Override
            public void success(byte[] bytes, String value) { }

            @Override
            public void fail(String value) {}
        });
        Toast.makeText(ConfirmActivity.this, "登录成功", Toast.LENGTH_LONG).show();
        startActivity(new Intent(ConfirmActivity.this, HomeActivity.class));
    }

}