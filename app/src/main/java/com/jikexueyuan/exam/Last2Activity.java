package com.jikexueyuan.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Last2Activity extends AppCompatActivity {

    private EditText etAdvice,etHname,etSection,etIname,etIdate;
    private Button btn_pre,btn_next;
    private String [] result;
    private String [] edits = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last2);

        etAdvice = (EditText) findViewById(R.id.etAdvice);
        etHname = (EditText) findViewById(R.id.etHname);
        etSection = (EditText) findViewById(R.id.etSection);
        etIname = (EditText) findViewById(R.id.etIname);
        etIdate = (EditText) findViewById(R.id.etIdate);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_pre = (Button) findViewById(R.id.btn_previous);

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Last2Activity.this.finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = Last2Activity.this.getIntent().getStringArrayExtra("result");
                edits[0] = etAdvice.getText().toString();
                edits[1] = etHname.getText().toString();
                edits[2] = etSection.getText().toString();
                edits[3] = etIname.getText().toString();
                edits[4] = etIdate.getText().toString();

                Intent i = new Intent(Last2Activity.this,Result2Activity.class);
                i.putExtra("result",result);
                i.putExtra("edits",edits);
                startActivity(i);
            }
        });
    }
}
