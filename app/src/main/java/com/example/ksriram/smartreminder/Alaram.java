package com.example.ksriram.smartreminder;
import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.RatingCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.Toast;


import java.util.ArrayList;


import static com.example.ksriram.smartreminder.App.CHANNEL_1_ID;
import static com.example.ksriram.smartreminder.App.CHANNEL_2_ID;


public class Alaram extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm=context.getPackageManager();
        String keyid = intent.getStringExtra("People").toString();
        ArrayList<String> listData = new ArrayList<>();

        if(keyid.equals("str")) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            int notoficationId = intent.getIntExtra("NotifiactionId", 0);
            String message = intent.getStringExtra("todo");
            String desc = intent.getStringExtra("desc");
            Intent mainIntent = new Intent(context, ListDataActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setSubText("This is Notification is only for you")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle("From SmartReminder")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .build();
int k = (int) Math.random();
            notificationManager.notify(k, notification);

        }
        else
        {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            int notoficationId = intent.getIntExtra("NotifiactionId", 0);
            String message = intent.getStringExtra("todo");

            String desc = intent.getStringExtra("desc");
            Intent mainIntent = new Intent(context, ListDataActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            listData = intent.getStringArrayListExtra("list");
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle("From SmartReminder")
                    .setSubText("This is a Group Notification")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .build();

            notificationManager.notify(1, notification);
            try
            {
                SmsManager smsMgrVar = SmsManager.getDefault();
                //smsMgrVar.sendTextMessage("7780498174", null, "hii sriram "+message, null, null);
                //smsMgrVar.sendTextMessage("9700111520", null, "hii Aravind: "+message, null, null);
                for(int i=0;i<listData.size();i++)

                    smsMgrVar.sendTextMessage(listData.get(i).toString(), null, "from SmartReminder: \n"+message+"\n"+desc, null, null);
                Toast.makeText(context, "Message Sent",
                        Toast.LENGTH_LONG).show();

                String text = message;// Replace with your message.

                String toNumber = listData.get(0).toString(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
               /*for(int i=0;i<listData.size();i++) {
                   Intent kintent = new Intent(Intent.ACTION_VIEW);
                   kintent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + listData.get(i).toString() + "&text=" + text));
                   kintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   //kintent.setAction(Intent.ACTION_SEND);
                   context.startActivity(kintent);
               }*/
//

                //

            }
            catch (Exception ErrVar)
            {
                Toast.makeText(context,ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
    }

}