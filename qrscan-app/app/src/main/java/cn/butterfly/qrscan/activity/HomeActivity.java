package cn.butterfly.qrscan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xui.XUI;

import org.apache.commons.lang3.StringUtils;

import cn.butterfly.qrscan.R;

/**
 * 主页
 *
 * @author zjw
 * @date 2021-09-20
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * 扫码处理
     *
     * @param view 扫码图片组件
     */
    public void scan(View view) {
        startActivity(new Intent(HomeActivity.this, ScanActivity.class));
    }

}