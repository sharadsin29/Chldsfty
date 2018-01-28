package com.wsafety;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditActivity extends AppCompatActivity {
    EditText name1, age1, sex1, height1, weight1, ph11, ph21;
    RadioGroup radioSexGroup;

    Button b1;
    LoginDataBaseAdapter loginDataBaseAdapter;
    RadioButton radioSexButton;
    String name, age, sex, ps1,ps2, height, weight, agetype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        name1 = (EditText) findViewById(R.id.name);
        age1 = (EditText) findViewById(R.id.age);
        sex1 = (EditText) findViewById(R.id.sex);
        height1 = (EditText) findViewById(R.id.height);
        weight1 = (EditText) findViewById(R.id.weight);
        ph11 = (EditText) findViewById(R.id.ph1);
        ph21 = (EditText) findViewById(R.id.ph2);



        b1=(Button)findViewById(R.id.btton1);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println(">>>>>>>>>>Data clicked");
        if(name1.equals("") || age1.equals("") || sex1.equals("") || height1.equals("") || weight1.equals("") || ph11.equals("") || ph21.equals("")) {
        Toast.makeText(getApplicationContext(),"Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else {
            name = name1.getText().toString();
            radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            System.out.println("selected id:"+selectedId);
            // find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);

            age = age1.getText().toString();
            sex = sex1.getText().toString();
            height = height1.getText().toString();
            weight = weight1.getText().toString();
            ps1 = ph11.getText().toString();
            ps2 = ph21.getText().toString();
            agetype = radioSexButton.getText().toString();
            System.out.println("selected user" + agetype);
            loginDataBaseAdapter.insertEntry(name, age, sex, ps1, ps2, height, weight, agetype);


            Intent i = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(i);
        }
            }
        });



    }






    }


