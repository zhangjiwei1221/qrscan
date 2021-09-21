package cn.butterfly.qrscan.activity;

import static cn.butterfly.qrscan.base.BaseConstants.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.xuexiang.xui.XUI;
import com.yutils.http.YHttp;
import com.yutils.http.contract.YHttpListener;
import org.apache.commons.lang3.StringUtils;
import cn.butterfly.qrscan.R;
import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.util.JsonUtils;

/**
 * 登录界面
 *
 * @author zjw
 * @date 2021-09-20
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * 用户名输入框
     */
    private EditText usernameEdit;

    /**
     * 密码输入框
     */
    private EditText passwordEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);
    }

    /**
     * 登录操作
     *
     * @param view 按钮组件
     */
    public void login(View view) {
        Editable username = usernameEdit.getText();
        if (StringUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Editable password = passwordEdit.getText();
        if (StringUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username.toString());
        jsonObject.addProperty("password", password.toString());
        String url = API + "/login";
        YHttp.create().post(url, jsonObject.toString(), new YHttpListener() {
            @Override
            public void success(byte[] bytes, String value) {
                BaseResult result = JsonUtils.parse(value, BaseResult.class);
                if (result.getCode() != 200) {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", result.getData().toString());
                editor.apply();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
            @Override
            public void fail(String value) {
                Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

}