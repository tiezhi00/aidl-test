package com.app.aidl_test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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

    private void showPopupDialog(Boolean b) {
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
}