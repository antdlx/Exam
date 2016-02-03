package com.jikexueyuan.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LastActivity extends AppCompatActivity {
    private EditText etAdvice,etSection,etIname,etIdate;
    private Button btn_pre,btn_next;
    private String [] result;
    private String [] edits = new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        etAdvice = (EditText) findViewById(R.id.etAdvice);
        etSection = (EditText) findViewById(R.id.etSection);
        etIname = (EditText) findViewById(R.id.etIname);
        etIdate = (EditText) findViewById(R.id.etIdate);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_pre = (Button) findViewById(R.id.btn_previous);

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LastActivity.this.finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               result = LastActivity.this.getIntent().getStringArrayExtra("result");
                edits[0] = etAdvice.getText().toString();
                edits[1] = etSection.getText().toString();
                edits[2] = etIname.getText().toString();
                edits[3] = etIdate.getText().toString();

                Intent i = new Intent(LastActivity.this,ResultActivity.class);
                i.putExtra("result",result);
                i.putExtra("edits",edits);
                startActivity(i);
            }
        });
    }
}
