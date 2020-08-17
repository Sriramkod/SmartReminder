package com.example.ksriram.smartreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMultiple extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT =1;
    private TextView phone;
    private Button select;
    String keyid;
    ArrayList<String> listData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multiple);
        setTitle("SmartReminder");
        Intent intent = getIntent();
        keyid = intent.getStringExtra("People");
        Toast.makeText(AddMultiple.this, "Hii ra this is:"+keyid, Toast.LENGTH_LONG).show();
        phone = findViewById (R.id.phone);
        select = findViewById (R.id.select);
        phone.setText(" ");
        select.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult (in, RESULT_PICK_CONTACT);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(resultCode==RESULT_OK)
        {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked (data);
                    break;
            }
        }
        else
        {
            Toast.makeText (this, "Failed To pick contact", Toast.LENGTH_SHORT).show ();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {

            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneName = String.valueOf(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNo = cursor.getString (phoneIndex);

            String contactName = null;

            // querying contact data store
            //Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

            if (cursor.moveToFirst()) {

                // DISPLAY_NAME = The display name for the contact.
                // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            //phone.setText (phoneNo);
            phone.append(" "+contactName);

            listData.add(phoneNo);


        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    public void Submit(View view) {
        if(listData.size()!=0){
        Intent intent = new Intent(AddMultiple.this,AddSingle.class);
        intent.putExtra("People",keyid);
        intent.putStringArrayListExtra("list",listData);
        startActivity(intent);}
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                    .setIcon(R.drawable.logo)
//set title
                    .setTitle("SmartReminder")
//set message
                    .setMessage("You must select atleast one contact")
//set positive button
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            finish();
                        }
                    })
//set negative button

                    .show();
        }
    }
}
