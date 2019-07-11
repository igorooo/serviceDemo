package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

//@TODO
// https://www.survivingwithandroid.com/2014/03/android-remote-service-tutorial-aidl.html

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mGetListButton, mDumpButton;
    private TextView mText;
    private com.example.client.IAidlConnector mService;
    private boolean mServiceAttached;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetListButton = (Button) findViewById(R.id.Button_1);
        mDumpButton = (Button) findViewById(R.id.Button_2);
        mText = (TextView) findViewById(R.id.TextView_1);

        setUiState(false);
        bindService();

        mGetListButton.setOnClickListener(getListButtonListener);
        mDumpButton.setOnClickListener(dumpButtonListener);
    }

    private View.OnClickListener getListButtonListener = new View.OnClickListener(){
        public void onClick(View v){
            if(mService != null){
                try{
                    List<String> list = mService.getSensorNames();
                    displayList(list,mText);
                } catch (RemoteException e){
                    Log.e(TAG, "Service crashed");
                }
            }
        }
    };

    private View.OnClickListener dumpButtonListener= new View.OnClickListener(){
        public void onClick(View v){
            if(mService != null){
                try{
                    List<String> list = mService.getSensorNames();
                } catch (RemoteException e){
                    Log.e(TAG, "Service crashed");
                }
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: called ...");
            mService = com.example.client.IAidlConnector.Stub.asInterface(iBinder);
            setUiState(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: called ...");
            mService = null;
            setUiState(false);
        }
    };

    private void bindService() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void displayList(List<String> list, TextView tv){
        StringBuilder sb = new StringBuilder();
        for(String elem : list){
            sb.append(elem);
            sb.append('\n');
        }
        tv.setText(sb.toString());
    }

    private void setUiState(boolean state){
        mGetListButton.setActivated(state);
        mDumpButton.setActivated(state);
    }
}
