package com.example.ksriram.smartreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddSingle extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,AdapterView.OnItemSelectedListener{
    DatabaseHelper mDatabaseHelper;
    ListView listview;
    Button addButton;
    EditText GetValue;
    TextView mDisplayDate;
    Button speakButton;
    EditText desc;
    private int notificationId =1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String[] ListElements = new String[] {};
    String s = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<String> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_add_single);
        setContentView(R.layout.activity_add_single);
        setTitle("SmartReminder");
        desc = findViewById(R.id.desc);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddSingle.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        mDatabaseHelper = new DatabaseHelper(this);
        //addButton = findViewById(R.id.button);
        GetValue = findViewById(R.id.editext);
        addButton = findViewById(R.id.button);



        Intent i = getIntent();
        s = i.getStringExtra("People");

        listData = i.getStringArrayListExtra("list");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int c=0;
                String newEntry = GetValue.getText().toString();
                if (GetValue.length() != 0) {
                    //AddData(newEntry);

                    Pend(c);
                    Intent intent = new Intent(AddSingle.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // GetValue.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
               /* Pend(c);
                Intent intent = new Intent(AddSingle.this,MainActivity.class);
                startActivity(intent);*/
            }
        });

    }
    public void AddData(String newEntry) {

        try {
            if (newEntry != null) {
                boolean insertData = mDatabaseHelper.addData(newEntry);

                if (insertData) {
                    toastMessage("Data Successfully Inserted!");
                } else {
                    toastMessage("Something went wrong");
                }
            }
        }
        catch (Exception e)
        {
            toastMessage(" "+e);
        }
    }
    public void toastMessage(String msg){
        Toast.makeText(this, " "+msg, Toast.LENGTH_SHORT).show();
    }

    public void Button(View view) {
        askSpeechInput();
    }
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Tell your title");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    GetValue.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void Pend(int request)
    {

        EditText editText = findViewById(R.id.editext);
        toastMessage("Hello World");

        //AddData(editText.getText().toString());
        TimePicker timePicker = findViewById(R.id.timePicker);

        String hourk = timePicker.getCurrentHour().toString();
        String minutek = timePicker.getCurrentMinute().toString();
        AddData("Title: "+editText.getText().toString()+" sent on "+mDisplayDate.getText().toString()+" at "+" "+hourk+":"+minutek+" and the description is: "+desc.getText().toString());

        Intent intent = new Intent(AddSingle.this,Alaram.class);
        intent.putExtra("NotifiactionId",notificationId);
        intent.putExtra("todo",editText.getText().toString());
        intent.putExtra("datee",mDisplayDate.getText().toString());
        intent.putExtra("desc",desc.getText().toString());
        intent.putExtra("People",s);
        intent.putStringArrayListExtra("list",listData);



        final int _id = (int) System.currentTimeMillis();
        PendingIntent alaramIntent = PendingIntent.getBroadcast(this, _id, intent,PendingIntent.FLAG_ONE_SHOT);

        //PendingIntent alaramIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        String title = editText.getText().toString();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        Calendar startTime  = Calendar.getInstance();

        startTime.set(Calendar.HOUR_OF_DAY,hour);
        startTime.set(Calendar.MINUTE,minute);
        startTime.set(Calendar.SECOND,0);
        long alaramstartTime = startTime.getTimeInMillis();
        alarm.set(AlarmManager.RTC_WAKEUP,alaramstartTime,alaramIntent);
        Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText("Hour: " + hourOfDay + " Minute: " + minute);

        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, i);
        datetime.set(Calendar.MINUTE, i1);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

        textView.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm+" at:"+getIntent().getStringExtra("imagename"));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
