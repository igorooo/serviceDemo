package com.example.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }

    private List<String> listAvilableSensors(){
        List<Sensor> tempList= ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_ALL);
        List<String> sensorList = new ArrayList<>();

        for(Sensor elem : tempList){
            sensorList.add(elem.getName());
        }

        return sensorList;
    }

    private void logAvilableSensors(){
        List<String> sensorsList = listAvilableSensors();
        for(String elem : sensorsList){
            Log.i("Avilable service: ",elem);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called ...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called ...");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: called ...");
        return iAidlConnector.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind: called ...");
        return super.onUnbind(intent);
    }

    com.example.server.IAidlConnector iAidlConnector = new com.example.server.IAidlConnector.Stub() {
        @Override
        public List<String> getSensorNames() throws RemoteException {
            return listAvilableSensors();
        }
    };
}
