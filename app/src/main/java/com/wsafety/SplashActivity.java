package com.wsafety;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Cursor c;
    int x;
   LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
// get Instance of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

       // db = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
       // db.execSQL("CREATE TABLE IF NOT EXISTS user(name VARCHAR,age VARCHAR,sex VARCHAR,p01 VARCHAR,po2 VARCHAR,height VARCHAR,weight VARCHAR, agetype VARCHAR);");
      //  c = db.rawQuery("SELECT * FROM user", null);
        x=loginDataBaseAdapter.selectAll();
        System.out.println("+++++++++++"+x);
         Thread t1=new Thread(){
            public void run(){
              try {
                 // Toast.makeText(getApplicationContext(),c.getCount(),Toast.LENGTH_LONG).show();
                sleep(3*1000);
              if(x==0){
        Intent i = new Intent(getBaseContext(), Main2Activity.class);
             //    Intent i = new Intent(getBaseContext(), EditActivity.class);
                  i.putExtra("value",Integer.toBinaryString(x));
        startActivity(i);
        //db.execSQL("INSERT INTO t VALUES('1');");
            }
          else{

        Intent i = new Intent(getBaseContext(), MainActivity.class);
       startActivity(i);

        }

                        }catch(Exception e){
                        }
                    }
                };
                t1.start();
            }
    }




