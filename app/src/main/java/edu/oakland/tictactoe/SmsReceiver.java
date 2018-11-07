package edu.oakland.tictactoe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    MainActivity activity = null;
    Settings1Activity settings1Activity = null;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    public SmsReceiver(Context context){
        if(context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
        if (context instanceof  Settings1Activity){
            settings1Activity = (Settings1Activity) context;
        }
        context.registerReceiver(this, filter);
    }

    //(650) 555-1212
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;
        Toast.makeText( context, "Sms Received", Toast.LENGTH_SHORT ).show();
        if (bundle != null){
            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdusObj.length; i++) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    String format = bundle.getString("format");
                    currentMessage = SmsMessage.createFromPdu((byte[])pdusObj[i], format);
                }else{
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }
                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                String[] tokens = message.split(" ");
                for (String s : tokens){
                    Log.d("SMS RECEIVER", s);
                }
                if(tokens.length != 0 && "STicTacToe".equalsIgnoreCase(tokens[0])){
                    String messageType = tokens[2];
                    if("INVITE".equalsIgnoreCase(messageType)){
                        Toast.makeText( context, "INVITE detected", Toast.LENGTH_LONG ).show();
                        String playerName = tokens[3];
                        String playerSymbol = tokens[4];
                        activity.processRequest(playerName, playerSymbol);
                    }
                }
            }
        }
    }
}
