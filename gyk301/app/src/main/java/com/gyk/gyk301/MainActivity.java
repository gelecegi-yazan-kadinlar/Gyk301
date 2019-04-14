package com.gyk.gyk301;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonVoiceRecord = (Button) findViewById(R.id.buttonVoiceRecord);
        Button buttonOpenMap = (Button) findViewById(R.id.buttonOpenMap);

        buttonVoiceRecord.setOnClickListener(this);
        buttonOpenMap.setOnClickListener(this);

        Button buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(this);

        Button buttonOpenWebSite = (Button) findViewById(R.id.buttonOpenWebSite);
        buttonOpenWebSite.setOnClickListener(this);

        Button buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(this);

        Button buttonSMS = (Button) findViewById(R.id.buttonSendSms);
        buttonSMS.setOnClickListener(this);


        // Üsttekiyle aynı işi yapar
        // findViewById(R.id.buttonCamera).setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCamera:
                Toast.makeText(this, "Kamera ekranı açılıyor!", Toast.LENGTH_SHORT).show();
                Intent camera = new Intent(MainActivity.this,ActivityCamera.class);
                startActivity(camera);
                break;
            case R.id.buttonVoiceRecord:
                Intent voiceRecord = new Intent(MainActivity.this,ActivityVoiceRecord.class);
                startActivity(voiceRecord);
                break;
            case R.id.buttonOpenMap:
                Intent openMap = new Intent(MainActivity.this,ActivityMaps.class);
                startActivity(openMap);
                break;
            case R.id.buttonCall:
                Intent openCall = new Intent(MainActivity.this,CallActivity.class);
                startActivity(openCall);
                break;
            case R.id.buttonSendSms:
                Intent openSms = new Intent(MainActivity.this,SendSmsActivity.class);
                startActivity(openSms);
                break;
            case R.id.buttonOpenWebSite:
                Intent openWebSite = new Intent(MainActivity.this,WebSiteActivity.class);
                startActivity(openWebSite);
                break;
        }
    }
}
