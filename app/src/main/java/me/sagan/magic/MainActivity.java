package me.sagan.magic;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.androidhiddencamera.HiddenCameraUtils;

import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText userText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("fuck","gogogo----");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        userText =(EditText) findViewById(R.id.username);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
                ) {
            // Permission is not granted
            Log.d("fuck", "We need camera permission to work!");
            requestPermissions( new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE}, 101);
        } else {
            Log.d("fuck", "permission ok");
        }
    }
    public void onRegister(View v){
        userText =(EditText) findViewById(R.id.username);
        SharedPreferences sharedPreferences = this.getSharedPreferences("data",0);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("username","test");
        myEdit.commit();
        Toast.makeText(getApplicationContext(),"User registered " + userText.getText(),Toast.LENGTH_LONG).show();
    }
    public void onToggleServiceClick(View v) {
        if( isMyServiceRunning(MagicService.class) ) {
            Intent intent = new Intent(this, MagicService.class);
            stopService(intent);
        } else {
            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                Intent intent = new Intent(this, MagicService.class);
                startForegroundService(intent);
            } else {
                //Open settings to grant permission for "Draw other apps".
                Toast.makeText(this, "Please grant the 'Draw over other apps' privilege to this app.", Toast.LENGTH_LONG).show();
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
