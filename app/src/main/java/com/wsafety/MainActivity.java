

package com.wsafety;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;


import java.io.File;

public class MainActivity extends Activity implements SensorEventListener {


    View v;

    TextView tname, tage, tbmi, tp1, tp2;
    SQLiteDatabase db;
    Intent i;
    String sname, sage, ssex, sp1, sp2, sheight, sweight, agetype;
    Cursor c;
    String p1, p2, getagetype;
    private static Context aContext=null;
    private static float threshold  = 15.0f;
    private static int interval     = 2000;
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

    /** indicates whether or not Accelerometer Sensor is supported */
    private static Boolean supported;
    /** indicates whether or not Accelerometer Sensor is running */
    private static boolean running = false;

    private SensorManager smanager;


    boolean min, max;
    int j;
    LoginDataBaseAdapter loginDataBaseAdapter;
    String newString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

           tname = (TextView) findViewById(R.id.username);
            tage = (TextView) findViewById(R.id.userage);
            tbmi = (TextView) findViewById(R.id.bmi);
            tp1 = (TextView) findViewById(R.id.phone1);
            tp2 = (TextView) findViewById(R.id.phone2);

/*

            sname = i.getStringExtra("name");
            sage = i.getStringExtra("age");
            ssex = i.getStringExtra("sex");
            sp1 = i.getStringExtra("phone1");
            sp2 = i.getStringExtra("phone2");
            sheight = i.getStringExtra("height");
            sweight = i.getStringExtra("weight");
            agetype = i.getStringExtra("agetype");

            System.out.println(agetype);
         //   getdata();
        } else {
            Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
        }

      // public void getdata()
*/

        smanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = smanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        smanager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //  db = openOrCreateDatabase("userdb", Context.MODE_PRIVATE, null);
        // db.execSQL("CREATE TABLE IF NOT EXISTS user(name VARCHAR,age VARCHAR,sex VARCHAR,p01 VARCHAR,po2 VARCHAR,height VARCHAR,weight VARCHAR, agetype VARCHAR);");
        // db.execSQL("delete from user");
    //    db.execSQL("INSERT INTO user VALUES('" + sname + "','" + sage + "','" + ssex + "','" + sp1 + "','" + sp2 + "','" + sheight + "','" + sweight + "','"+agetype+"');");

       /* catch (SQLiteException e){
           // if (e.getMessage().toString().contains("no such table")){
                Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                // create table
                // re-run query, etc.
            //}
        }*/

       ArrayList dataset=new ArrayList();
        dataset=loginDataBaseAdapter.getselctAll();

            tname.setText("Name : "+dataset.get(0).toString());
            tage.setText(" Age : "+dataset.get(1).toString());
            tp1.setText("Phone1 :"+dataset.get(2).toString());
            tp2.setText("Phone2 : "+dataset.get(3).toString());
            p1 = dataset.get(2).toString();
            p2 = dataset.get(3).toString();
            getagetype=dataset.get(4).toString();
            System.out.println("db returned data"+getagetype);






        }


//        float Bmi = Float.parseFloat(sweight) / (Float.parseFloat(sheight) * Float.parseFloat(sheight));
        //      tbmi.setText("BMI: " + Float.toHexString(Bmi));



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        smanager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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

                if (Float.compare(force, threshold) >0 ) {
                    //Toast.makeText(Accelerometer.getContext(), (now-lastShake)+"  >= "+interval, 1000).show();
                    if (now - lastShake >= interval) {

                        // trigger shake event
                        int i=Math.round(force);
                        System.out.println("++++++++++++++++++++i value++++++++"+i+" type"+getagetype);
                        // Toast.makeText(aContext, "mtion value"+i, Toast.LENGTH_LONG).show();

                        if(i>30) {
                            if (getagetype.equals("TeenAge")) {
                                //listener.onShake(force);OldAge
                                System.out.println("==========--------Teeage calline==============="+getagetype);
                                Ring(v);
                            } else if (getagetype.equals("OldAge")) {
                                System.out.println("==========--------Old age calline==============="+getagetype);
                                Intent x = new Intent(MainActivity.this, AlertOldAge.class);
                                x.putExtra("num1", p1);
                                x.putExtra("num2", p2);
                                startActivity(x);
                            }
                        }
                    }
                    else
                    {
                        //Toast.makeText(aContext,"No Motion detected", Toast.LENGTH_SHORT).show();

                    }
                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now;
            }
            else
            {
                //	Toast.makeText(aContext,"No Motion detected", Toast.LENGTH_SHORT).show();

            }
        }
        // trigger change event
        //  listener.onAccelerationChanged(x, y, z);

    }


    public void Ring(View v) {

        Intent i=new Intent(MainActivity.this,AlertActivity.class);
        i.putExtra("num1",p1);
        i.putExtra("num2",p2);
        startActivity(i);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }



}