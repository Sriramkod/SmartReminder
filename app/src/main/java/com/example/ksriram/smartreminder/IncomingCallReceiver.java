package com.example.ksriram.smartreminder;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;

public class IncomingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //com.android.internal.telephony.ITelephony telephonyService;
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    @SuppressLint("SoonBlockedPrivateApi") Method m = tm.getClass().getDeclaredMethod("getITelephony");

                    m.setAccessible(true);
          //          telephonyService = (com.android.internal.telephony.ITelephony) m.invoke(tm);

                    if ((number != null)) {
            //            telephonyService.endCall();
                        Toast.makeText(context, "Ending the call from: " + number, Toast.LENGTH_SHORT).show();
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, "I'm busy right now...please call me later!!!", null, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Ring " + number, Toast.LENGTH_SHORT).show();

            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(context, "Answered " + number, Toast.LENGTH_SHORT).show();
            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context, "Idle "+ number, Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
