package com.gyk.gyk301;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMaps extends AppCompatActivity {
    private EditText editTextLang;
    private EditText editTextLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editTextLang = (EditText) findViewById(R.id.editTextLang);
        editTextLong = (EditText) findViewById(R.id.editTextLong);

        Button buttonShowMap = (Button) findViewById(R.id.buttonShowInMap);

        buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = editTextLang.getText().toString();
                String longitude =  editTextLong.getText().toString();

                Uri geoLocation = Uri.parse("geo:"+latitude+","+longitude);
                showMap(geoLocation);
            }
        });
    }

    public void showMap (Uri geoLocation){
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(geoLocation);
        if(mapIntent.resolveActivity(getPackageManager()) != null){
            startActivity(mapIntent);
        }
    }
}




