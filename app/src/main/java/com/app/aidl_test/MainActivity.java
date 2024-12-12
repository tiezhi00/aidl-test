package com.app.aidl_test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.app.aidl_test.util.Constants;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private Switch mySwitch;
    private Boolean isSetNegative=false;//用于标记是否是点击了取消导致的重复checked


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //设置点击事件
        setListener();
    }

    private void initView() {
        mySwitch = findViewById(R.id.mySwitch);
    }

    private void setListener() {
        //移动数据
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!isSetNegative) {
                    showPopupDialog(b);
                }
                isSetNegative=false;
            }
        });
    }

    protected void showPopupDialog(Boolean b) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(b?"是否开启移动数据":"是否关闭移动数据");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 开启移动数据
                Toast.makeText(MainActivity.this,"移动数据"+(b?"已开启":"已关闭"),Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 取消
                isSetNegative = true;
                mySwitch.setChecked(!b);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: --------这里执行了-----");
        //处理显示弹窗的事件
        if(intent.hasExtra("dialogType")) {
            Log.i(TAG, "onNewIntent: 处理显示弹窗的事件,dialogType="+intent.getIntExtra("dialogType",0)+",isActive="+intent.getBooleanExtra("isActive",false)+"");
            switch (intent.getIntExtra("dialogType",0)){
                case Constants.DialogType_OpenData:
                    Log.i(TAG, "onNewIntent: 处理显示移动数据弹窗的事件");
                    //开启移动数据
                    //获取传递过来的isActive值
                    boolean isActive = intent.getBooleanExtra("isActive", false);
                    //展示弹窗
                    showPopupDialog(isActive);
                    break;
                default:
                    break;

            }
        }
    }
}