package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mStopServiceButton, mStartServiceButton, mBindServiceButton, mGetServiceMsgButton, mUnBindServiceButton, mClearButton;
    private TextView mInnerTextView, mExternTextView;
    private com.example.server.IAidlConnector mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartServiceButton = (Button) findViewById(R.id.startService_Button);
        mStopServiceButton = (Button) findViewById(R.id.stopService_Button);
        mBindServiceButton = (Button) findViewById(R.id.bindService_Button);
        mUnBindServiceButton = (Button) findViewById(R.id.unBindService_Button);
        mGetServiceMsgButton = (Button) findViewById(R.id.getServiceMsg_Button);
        mClearButton = (Button) findViewById(R.id.clear_Button);
        mInnerTextView = (TextView) findViewById(R.id.inner_TextView);
        mExternTextView = (TextView) findViewById(R.id.extern_TextView);


        mStartServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });

        mStopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
            }
        });

        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService();
            }
        });

        mUnBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService();
            }
        });

        mGetServiceMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getServiceMsg();
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTextViews();
            }
        });
    }

    private void clearTextViews() {
        try{
            mInnerTextView.setText("");
            mExternTextView.setText("");
        } catch (NullPointerException e){
            Log.e(TAG, "Null pointer except. during cleanTextViews");
        }
    }

    private void getServiceMsg() {
        try {
            List<String> res = mService.getSensorNames();
            StringBuilder sb = new StringBuilder();
            for(String elem : res){
                sb.append(elem);
                sb.append('\n');
            }
            mInnerTextView.setText(sb.toString());

        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception during getServiceMsg()");
        } catch (NullPointerException e){
            if(mService == null){
                Log.e(TAG, "Null pointer on mService");
            }else {
                Log.e(TAG, "Null pointer during getServiceMsg()");
            }
        }
    }

    private void startService() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void bindService() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        unbindService(mConnection);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: called ...");
            mService = com.example.server.IAidlConnector.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: called ...");
            mService = null;
        }
    };


}
