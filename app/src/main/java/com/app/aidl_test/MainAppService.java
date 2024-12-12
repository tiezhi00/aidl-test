package com.app.aidl_test;

import static java.lang.Thread.sleep;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.app.aidl_test.util.Constants;

public class MainAppService extends Service {
    private static final String TAG = "MainAppService";
    private String mStrData="";
    private boolean serviceIsRunning = true;
    private IBinder binder = new IAppAidlInterface.Stub() {
        @Override
        public void setStringData(String strData) throws RemoteException {
            //在这里实现服务的具体方法
            mStrData = strData;
        }


        @Override
        public void showPopupDialog(boolean isActive) throws RemoteException {
            Log.i(TAG, "showPopupDialog: isActive=" + isActive);
            //通知MainActivity显示弹窗
            Intent intent = new Intent(MainAppService.this, MainActivity.class);
            intent.putExtra("dialogType", Constants.DialogType_OpenData);
            intent.putExtra("isActive", isActive);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // 添加这行代码
            startActivity(intent);
        }
    };

    public MainAppService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        serviceIsRunning = true;
        //在服务绑定时
        //启动一个线程，每1秒轮循打印客户端发送过来的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (serviceIsRunning) {
                    try {
                        sleep(1000);
                        Log.i(TAG, "run: 当前收到客户端设置的mStrData=" + mStrData);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        serviceIsRunning = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}