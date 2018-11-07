package edu.oakland.tictactoe;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsUtil {

    public static boolean isSmsPermissionGranted(Context context){
        return ContextCompat.checkSelfPermission( context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadAndSendSmsPermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_SMS)){

        }
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE}, 1);
    }
}
