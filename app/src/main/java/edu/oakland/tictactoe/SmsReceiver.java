package edu.oakland.tictactoe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static MainActivity activity;
    private static Settings1Activity settings1Activity;
    private static Settings2Activity settings2Activity;
    private static GameActivity gameActivity;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    public SmsReceiver(Context context){
        if(context instanceof MainActivity) {
            activity = (MainActivity) context;
            //Log.i("TicTacToe","Main registered");
        }
        if (context instanceof  Settings1Activity){
            settings1Activity = (Settings1Activity) context;
            //Log.i("TicTacToe", "Settings1 registered");
        }
        if (context instanceof Settings2Activity){
            settings2Activity = (Settings2Activity) context;
            //Log.i("TicTacToe", "Settings2 registered");
        }
        if (context instanceof GameActivity){
            gameActivity = (GameActivity) context;
            //Log.i("TicTacToe", "Game registered");
        }

        context.registerReceiver(this, filter);
    }

    //(650) 555-1212
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;
        //Toast.makeText( context, "Sms Received", Toast.LENGTH_SHORT ).show();
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
                //Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                String[] tokens = message.split(" ");
                /*for (String s : tokens){
                    Log.d("SMS RECEIVER", s);
                }*/
                if(tokens.length != 0 && "STicTacToe".equalsIgnoreCase(tokens[0])){
                    String messageType = tokens[2];
                    //Toast.makeText(context, "MessageTYpe"+messageType+senderNum, Toast.LENGTH_LONG).show();
                    String playerName = tokens[3];
                    String playerSymbol = tokens[4];
                    if("INVITE".equalsIgnoreCase(messageType)){
                        //Toast.makeText( context, "INVITE detected", Toast.LENGTH_LONG ).show();
                        activity.processInviteRequest(playerName, playerSymbol, senderNum);
                    } else if("START".equalsIgnoreCase(messageType)){
                        //Toast.makeText(context, "Invite ACCEPTed", Toast.LENGTH_LONG).show();
                        gameActivity.processStartRequest(playerName, playerSymbol, senderNum);
                    }else if("ACCEPT".equalsIgnoreCase(messageType)){
                        //Toast.makeText(context, "Invite ACCEPTed", Toast.LENGTH_LONG).show();
                        settings1Activity.processAcceptRequest(playerName, playerSymbol, senderNum);
                    }else if("DECLINE".equalsIgnoreCase(messageType)) {
                        settings1Activity.processDeclineRequest(playerName, playerSymbol, senderNum);
                    } else if("MOVE".equalsIgnoreCase(messageType)){
                        //Toast.makeText(context, "Invite ACCEPTed", Toast.LENGTH_LONG).show();
                        int dataCell = Integer.parseInt(tokens[5]);
                        gameActivity.processMoveRequest(playerName, playerSymbol, senderNum, dataCell);
                    } else if("WIN".equalsIgnoreCase(messageType)){
                        //Toast.makeText(context, "Invite ACCEPTed", Toast.LENGTH_LONG).show();
                        int dataCell = Integer.parseInt(tokens[5]);
                        gameActivity.processWinRequest(playerName, playerSymbol, senderNum, dataCell);
                    } else if("CANCEL".equalsIgnoreCase(messageType)){
                        int dataCell = Integer.parseInt(tokens[5]);
                        gameActivity.processCancelRequest(playerName, playerSymbol, senderNum, dataCell);
                    } else if("RESET".equalsIgnoreCase(messageType)){
                        int dataCell = Integer.parseInt(tokens[5]);
                        gameActivity.processResetRequest(playerName, playerSymbol, senderNum, dataCell);
                    }
                }
            }
        }
    }
}
