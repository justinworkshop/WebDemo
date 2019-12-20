package com.example.webdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.webdemo.bridge.BridgeActivity;
import com.example.webdemo.jsinterface.JsInterfaceActivity;
import com.example.webdemo.keyboard.KeyboardActivity;
import com.example.webdemo.utils.SpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mFruitNet;
    private Button mFruitAssets;
    private Button mFruitSdcard;
    private EditText mEtIp;
    private Button mFruitLoadByDefine;
    private Button mBtnTestJs;
    private Button mBtnTestBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mFruitNet = findViewById(R.id.btn_load_from_net);
        mFruitAssets = findViewById(R.id.btn_load_from_assets);
        mFruitSdcard = findViewById(R.id.btn_load_from_sdcard);
        mEtIp = findViewById(R.id.et_load_define_ip);
        mFruitLoadByDefine = findViewById(R.id.btn_load_define);
        mBtnTestJs = findViewById(R.id.btn_test_js_interface);
        mBtnTestBridge = findViewById(R.id.btn_test_bridge);

        mFruitNet.setOnClickListener(this);
        mFruitAssets.setOnClickListener(this);
        mFruitSdcard.setOnClickListener(this);
        mFruitLoadByDefine.setOnClickListener(this);
        mEtIp.setOnClickListener(this);
        mBtnTestJs.setOnClickListener(this);
        mBtnTestBridge.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mEtIp.setText(SpUtil.getString("ip", "10.10.31.91"));
        mEtIp.setSelection(mEtIp.getText().toString().trim().length());
    }

    @Override
    public void onClick(View v) {
        String mode = "";
        switch (v.getId()) {
            case R.id.btn_load_from_net:
                mode = "net";
                break;
            case R.id.btn_load_from_assets:
                mode = "assets";
                break;
            case R.id.btn_load_from_sdcard:
                mode = "sdcard";
                break;
            case R.id.btn_load_define:
                mode = "define";
                break;
            case R.id.et_load_define_ip:
                Intent intent = new Intent(MainActivity.this, KeyboardActivity.class);
                startActivity(intent);
                return;
            case R.id.btn_test_js_interface:
                Intent jsIntent = new Intent(MainActivity.this, JsInterfaceActivity.class);
                startActivity(jsIntent);
                return;
            case R.id.btn_test_bridge:
                Intent bridgeIntent = new Intent(MainActivity.this, BridgeActivity.class);
                startActivity(bridgeIntent);
                return;
            default:
                break;
        }

        Intent intent = new Intent(this, FruitGameActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("ip", mEtIp.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
