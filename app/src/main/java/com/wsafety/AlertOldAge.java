package com.wsafety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class AlertOldAge extends AppCompatActivity  {
    MediaPlayer sound;
    Vibrator vib;
    String n1, n2;
    View v;
    SQLiteDatabase db;
    Cursor c;
    String data;
    boolean clicked=false;
    String msg;
    String locationAddress11;

    // GPSTracker class
    GPSTracker gps;

    String locValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_old_age);
        Intent i = getIntent();


        gps = new GPSTracker(AlertOldAge.this);

        // check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
           locValue = new GeocoderHandler().getLocValue();
          //  data= "Your Address of Latitude: " + latitude +                    " Longitude: " + longitude;

            data="https://www.google.co.in/maps/@"+latitude+","+longitude+",20z?hl=en&authuser=0";
            System.out.println("msg daa-====================="+data);

        }

        Toast.makeText(getApplicationContext(),"msg daa-============"+data,Toast.LENGTH_LONG).show();
        n1 = i.getStringExtra("num1");
        n2 = i.getStringExtra("num2");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(15000);
                    sound = MediaPlayer.create(AlertOldAge.this, R.raw.aa);
                    sound.start();
                    Thread.sleep(10000);
                    if(clicked)
                    {
                        System.out.println("button canceled");
                    }
                    else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 locValue = new GeocoderHandler().getLocValue();
                            }
                        });

                        System.out.println("====================data=============="+data+"locavaluei s==="+locValue);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(n1, null, "I'm in danger!"+data, null, null);
                        smsManager.sendTextMessage(n2, null, "I'm in danger!"+data, null, null);

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + n1));
                        if (ActivityCompat.checkSelfPermission(AlertOldAge.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

/*
    @Override
    public void onDestroy() {
        sound.stop();
        vib.cancel();


        super.onDestroy();
    }
*/

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "use cancel button", Toast.LENGTH_LONG).show();
        /**super.onBackPressed();
         onDestroy();
         finish();*/
        /*Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        /*System.exit(0);*/
    }


    public void Alert(View v) {

      clicked=true;
        sound.stop();
        vib.cancel();



        Toast.makeText(getApplicationContext(),"Call msg stopped", Toast.LENGTH_LONG).show();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }




    private class GeocoderHandler extends Handler {

        private String locValue;

        public String getLocValue()
        {
            System.out.println("address of data"+locValue);
            return locValue;
        }
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    String locationAddress1 = bundle.getString("address");
                    System.out.println(locationAddress1);
                    locationAddress1=locationAddress11;
                    locValue = locationAddress1;
                    Toast.makeText(getApplicationContext(), locationAddress1,Toast.LENGTH_LONG).show();
                    break;
                default:
                    locationAddress1 = null;
            }
            // tvAddress.setText(locationAddress);

        }
    }

}
