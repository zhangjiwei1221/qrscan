package cn.butterfly.qrscan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.XUI;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

}