package com.gyk.gyk301;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity {
    EditText editTextPhoneNumber;
    private static final int CALL_PERMISSON_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        Button buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    String phoneNumber = editTextPhoneNumber.getText().toString();
                    callPhoneNumber(phoneNumber);
                }else{
                    requestPermission();
                }

            }
        });
    }
    public void callPhoneNumber(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Toast.makeText(this, "Aranamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission() {
        // result WRITE_EXTERNAL_STORAGE izni var mı? varsa 0 yoksa -1
        int result = ContextCompat.
                checkSelfPermission(getApplicationContext()
                        , Manifest.permission.CALL_PHONE);
        // result1 CALL_PHONE izni var mı? varsa 0 yoksa -1
        //İkisinede izin verilmiş ise true diğer durumlarda false döner
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        //Verilen String[] dizisi içerisindeki izinlere istek atılır
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                CALL_PERMISSON_REQUEST_CODE);
    }


    //İstek atılır istek onay/red işlemi bittiğinde bu metod çalışır
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode istek atılırken kullanılan kod ile aynıysa
        if (requestCode == CALL_PERMISSON_REQUEST_CODE) {
            if (grantResults.length > 0) { // İzin verilenlerin listesi en az 1 elemanlı ise
                //Record izni verildi mi?
                boolean permissionToCall = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                //External Store izni verildi mi

                if (permissionToCall) {
                    Toast.makeText(this, "İzinler alındı!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "İzin vermen gerekli!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
