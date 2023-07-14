package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodyo.sdk.Sodyo;
import com.sodyo.sdk.SodyoEventCallback;
import com.sodyo.sdk.SodyoScannerActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SodyoEventCallback {

    private static final String TAG = "MainActivity";

    private static final int SODYO_SCANNER_REQUEST_CODE = 1111;

    private static final String SODYO_APP_KEY = "28e9f48c0dae4cec8d223c8331c97482";
    private static final String SODYO_APP_KEY_UNIVERSAL = "84d62d87-1856-4b10-8a11-df0157c1f605";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init Sodyo engine
        Sodyo.init(this.getApplication(), SODYO_APP_KEY, null);
        addUserInfo();
        addCustomOverlay();
        Sodyo.setCustomAdLabel("nt1,nt2,nt3");
        Sodyo.getInstance().setSodyoEventCallback(this);

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String markerCode = data.getQueryParameter("markerCode");
            Sodyo.performMarker(markerCode, MainActivity.this);
        }
    }

    private void addUserInfo() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("firstName", "Tomer");
        userInfo.put("lastName", "Lavi");
        userInfo.put("phone", "054");
        userInfo.put("gender", "M");
        userInfo.put("email", "tomer@sodyo");
        userInfo.put("company", "sodyo");
        userInfo.put("birthday", "21/1/2000");
        userInfo.put("custom1", "cccccc");
        Sodyo.setUserInfo(userInfo);
        Sodyo.setAppUserId("222222222");
    }

    private void addCustomOverlay() {
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        TextView label = new TextView(getApplicationContext());
        label.setText("Hello World :]");
        label.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams labelLayoutParam=new LinearLayout.LayoutParams(350,150);
        label.setLayoutParams(labelLayoutParam);
        linearLayout.addView(label);

        Sodyo.setOverlayView(linearLayout);
    }

    public void launchSodyo(View v) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MainActivity.this, SodyoScannerActivity.class);
            startActivityForResult(intent, SODYO_SCANNER_REQUEST_CODE);
        } else {
            Log.d(TAG, "launchSodyo: requestPermissions" );
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }

    }

    public void performMarker(View v) {
        Sodyo.performMarker("33125536425", MainActivity.this);
    }

    @Override
    public void onSodyoEvent(String eventName, String eventData) {
        Log.d(TAG, "onSodyoEvent: " + eventName + ":\n" + eventData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "launchSodyo: onRequestPermissionsResult");
        Intent intent = new Intent(MainActivity.this, SodyoScannerActivity.class);
        startActivityForResult(intent, SODYO_SCANNER_REQUEST_CODE);
    }
}
