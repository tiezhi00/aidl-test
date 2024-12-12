package com.app.otherapp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.app.aidl_test.IAppAidlInterface;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.otherapp.databinding.ActivityOtherAppMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class OtherAppMainActivity extends AppCompatActivity {
    private static final String TAG = "OtherAppMainActivity";
    private IAppAidlInterface mIBinder;
    private int count = 0;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (mIBinder == null) {
                mIBinder = IAppAidlInterface.Stub.asInterface(iBinder);
            }

            count++;
            String str = "第" + count + "次连接成功！";
            try {
                mIBinder.setStringData(str);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_other_app_main);
        //首先绑定服务
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.app.aidl_test", "com.app.aidl_test.MainAppService"));
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBindedSucssfuly = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.i(TAG, "onCreate: 是否连接成功--------isBindedSucssfuly=" + isBindedSucssfuly);

            }
        });
        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(serviceConnection);
                mIBinder = null;
            }
        });
        //显示开启的弹窗
        findViewById(R.id.btn_showActivePopupDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIBinder != null) {
                    try {
                        mIBinder.showPopupDialog(true);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        //显示关闭的弹窗
        findViewById(R.id.btn_showUnactivePopupDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIBinder != null) {
                    try {
                        mIBinder.showPopupDialog(false);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }


}