package com.example.ksriram.smartreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SendMessage extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        tv = findViewById(R.id.textView);
        Intent intent = getIntent();
        tv.setText(" "+intent.getStringExtra("msg"));
    }
}
