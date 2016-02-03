package com.jikexueyuan.exam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HospitalActivity extends AppCompatActivity {

    private Button btn_hospiral;
    private EditText et_hospital;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean jump=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        btn_hospiral = (Button) findViewById(R.id.btn_hospital);
        et_hospital = (EditText) findViewById(R.id.et_hospital);

        sharedPreferences= getSharedPreferences("Configs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        jump = getIntent().getBooleanExtra("JUMP",true);

        if ((!sharedPreferences.getString("NAME","NULL").equals("NULL"))&&jump){
                Intent i = new Intent(HospitalActivity.this,MainActivity.class);
                startActivity(i);
                HospitalActivity.this.finish();
        }

        btn_hospiral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_hospital.getText().toString().isEmpty()){
                    editor.putString("NAME",et_hospital.getText().toString());
                    editor.commit();
                    if (jump){
                        Intent i = new Intent(HospitalActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    HospitalActivity.this.finish();
                }
            }
        });

    }
}
