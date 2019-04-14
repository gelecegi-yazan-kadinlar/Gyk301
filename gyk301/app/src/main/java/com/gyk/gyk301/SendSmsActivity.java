package com.gyk.gyk301;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendSmsActivity extends AppCompatActivity {
    EditText editTextPhoneNumber;
    EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        Button buttonSendSMS = (Button) findViewById(R.id.buttonSendSms);
        buttonSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String message = editTextMessage.getText().toString();

                sendSms(phoneNumber,message);
            }
        });
    }

    private void sendSms(String phoneNumber, String message) {
        Uri uri = Uri.parse("smsto:"+phoneNumber);
        Intent intent = new Intent (Intent.ACTION_SENDTO,uri);
        intent.putExtra("sms_body",message);
        startActivity(intent);
    }

}
