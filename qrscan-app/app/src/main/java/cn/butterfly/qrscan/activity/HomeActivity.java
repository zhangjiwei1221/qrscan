package cn.butterfly.qrscan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import com.xuexiang.xui.XUI;
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
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        }
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