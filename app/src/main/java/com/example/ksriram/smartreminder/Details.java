package com.example.ksriram.smartreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    TextView event;
    int id;
    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        mDatabaseHelper = new DatabaseHelper(this);
        setTitle("SmartReminder");
        event  = findViewById(R.id.event);
        Intent intent= getIntent();
        id = intent.getIntExtra("id",0);
        name = intent.getStringExtra("name");
        event.setText("  "+name);

    }

    public void delete(View view) {
        //mDatabaseHelper.deleteName(id,name);
        Intent intent = new Intent(Details.this,AddReminder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
