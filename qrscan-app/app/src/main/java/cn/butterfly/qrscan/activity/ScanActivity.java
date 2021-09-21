package cn.butterfly.qrscan.activity;

import static cn.butterfly.qrscan.base.BaseConstants.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.xuexiang.xui.XUI;
import com.yutils.http.YHttp;
import com.yutils.http.contract.YHttpListener;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.butterfly.qrscan.R;
import cn.butterfly.qrscan.base.BaseResult;
import cn.butterfly.qrscan.util.JsonUtils;

/**
 * 扫码界面
 *
 * @author zjw
 * @date 2021-09-20
 */
public class ScanActivity extends AppCompatActivity {

    private ZXingView scanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        init();
        initListener();
    }

    private void init() {
        scanView = findViewById(R.id.scanView);
    }

    private void initListener() {
        scanView.setDelegate(new QRCodeView.Delegate() {
            @Override
            @SuppressWarnings("unchecked")
            public void onScanQRCodeSuccess(String result) {
                vibrate();
                SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
                String token = sp.getString("token", StringUtils.EMPTY);
                String code = result.substring(result.indexOf("=") + 1);
                String url = API + "/code/scan?code=" + code + "&token=" + token;
                YHttp.create().get(url, new YHttpListener() {
                    @Override
                    public void success(byte[] bytes, String value) {
                        BaseResult result = JsonUtils.parse(value, BaseResult.class);
                        if (result.getCode() != 200) {
                            Toast.makeText(ScanActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                            reScan();
                            return;
                        }
                        Map<String, String> info = (Map<String, String>) result.getData();
                        String address = info.get("address");
                        String browser = info.get("browser");
                        String os = info.get("os");
                        String token = info.get("token");
                        Intent intent = new Intent(ScanActivity.this, ConfirmActivity.class);
                        intent.putExtra("address", address);
                        intent.putExtra("browser", browser);
                        intent.putExtra("os", os);
                        intent.putExtra("token", token);
                        intent.putExtra("code", code);
                        startActivity(intent);
                    }

                    @Override
                    public void fail(String value) {
                        Toast.makeText(ScanActivity.this, "无效二维码", Toast.LENGTH_LONG).show();
                        reScan();
                    }
                });
            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {}

            @Override
            public void onScanQRCodeOpenCameraError() {}
        });
    }

    /**
     * 重新开始下一次扫描
     */
    private void reScan() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanView.startSpot();
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanView.startCamera();
        scanView.showScanRect();
        scanView.startSpot();
    }

    @Override
    protected void onStop() {
        scanView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        scanView.onDestroy();
        super.onDestroy();
    }

    /**
     * 震动效果
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

}
