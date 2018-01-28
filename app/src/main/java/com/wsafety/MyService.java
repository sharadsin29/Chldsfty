package com.wsafety;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TutorialsPoint7 on 8/23/2016.
 */

public class MyService extends Service implements SensorEventListener {

    View v;

    int i = 0;

    String p1, p2, getagetype;
    private static float threshold = 15.0f;
    private static int interval = 2000;
    private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;

    private static Sensor sensor;
    private static SensorManager sensorManager;
    // you could use an OrientationListener array instead
    // if you plans to use more than one listener
    private static AccelerometerListener listener;


    private SensorManager smanager;


    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        new Timer().schedule(new TimerTask() {
            public void run() {
                getCurrentTopActivity();
                System.out.println("Activity started");
            }
        }, 0, 500);

        return START_STICKY;
    }




    public void getCurrentTopActivity() {
        System.out.println("current activity entered");
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        smanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = smanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        smanager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


        ArrayList dataset = new ArrayList();
        dataset = loginDataBaseAdapter.getselctAll();

        if(dataset.size()!=0) {
            p1 = dataset.get(2).toString();
            p2 = dataset.get(3).toString();

            getagetype = dataset.get(4).toString();

            System.out.println("db returned data" + getagetype);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        now = event.timestamp;

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        // if not interesting in shake events
        // just remove the whole if then else block
        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;
            // Toast.makeText(aContext,"No Motion detected", Toast.LENGTH_SHORT).show();

        } else {
            timeDiff = now - lastUpdate;

            if (timeDiff > 0) {

                    /*force = Math.abs(x + y + z - lastX - lastY - lastZ)
                                / timeDiff;*/
                force = Math.abs(x + y + z - lastX - lastY - lastZ);

                if (Float.compare(force, threshold) > 0) {
                    //Toast.makeText(Accelerometer.getContext(), (now-lastShake)+"  >= "+interval, 1000).show();
                    if (now - lastShake >= interval) {

                        // trigger shake event
                        i = Math.round(force);
                        System.out.println("++++++++++++++++++++i value++++++++" + i + " type" + getagetype);
                        // Toast.makeText(aContext, "mtion value"+i, Toast.LENGTH_LONG).show();

                        if (i > 30) {
                            if (getagetype.equals("TeenAge")) {
                                //listener.onShake(force);OldAge

                                Ring(v);
                            } else if (getagetype.equals("OldAge")) {
                                Intent x = new Intent(getBaseContext(), com.wsafety.AlertOldAge.class);
                                x.putExtra("num1", p1);
                                x.putExtra("num2", p2);
                                x.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(x);
                            }
                        }
                    } else {
                        //Toast.makeText(aContext,"No Motion detected", Toast.LENGTH_SHORT).show();

                    }
                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now;
            }
        }
        // trigger change event
        //  listener.onAccelerationChanged(x, y, z);

    }



    public void Ring(View v) {

        Intent i = new Intent(getBaseContext(), AlertActivity.class);
        i.putExtra("num1", p1);
        i.putExtra("num2", p2);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


