package com.gyk.gyk301;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityCamera extends AppCompatActivity {
    private static final int VIDEO_ACTION_CODE = 101;
    private static final int IMAGE_ACTION_CODE = 102;
    private static final int CAMERA_PERMISSON_REQUEST_CODE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button takePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        Button takeVideo = (Button) findViewById(R.id.buttonRecordVideo);
        if (!checkPermission()) { //izinler kontrol edilir
            requestPermission(); //İzin verilmemiş ise izin istenir
        }
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {//izinler kontrol edilir

                    requestPermission();//İzin verilmemiş ise izin istenir
                } else {
                    takeNewPhoto(); //İzin verilmiş ise fotoğraf çek
                }

            }
        });

        takeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {//izinler kontrol edilir

                    requestPermission();//İzin verilmemiş ise izin istenir
                } else {
                    recordNewVideo(); //İzin verilmiş ise video çek
                }
            }
        });
    }

    public void takeNewPhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Fotoğraf çekme intenti
        startActivityForResult(takePhotoIntent, IMAGE_ACTION_CODE); //intenti belirlenen kod ile başlat
    }

    public void recordNewVideo() {
        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); //Video yakalama intenti
        startActivityForResult(recordVideoIntent, VIDEO_ACTION_CODE); //intenti belirlenen kod ile başlat
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return; //Fotoğraf veya Video onaylanır ise RESULT_OK döner

        switch (requestCode) {
            case IMAGE_ACTION_CODE:
                Bundle extras = data.getExtras();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap((Bitmap) extras.get("data"));
                if(createFolder()){
                    saveImage((Bitmap) extras.get("data"));
                }else{
                    Toast.makeText(this, "Kaydedilemedi!", Toast.LENGTH_SHORT).show();
                }

                break;
            case VIDEO_ACTION_CODE:
                VideoView videoView = (VideoView) findViewById(R.id.videoView);
                videoView.setVideoURI(data.getData());
                videoView.setMediaController(new MediaController(this));
                videoView.requestFocus();
                videoView.start();
                break;

        }
    }
    public boolean createFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "gyk301/Fotograflar");

        if (!folder.exists()) {
            return folder.mkdirs();
        }else{
            return true;
        }

    }
    private void saveImage(Bitmap bitmap) {
        OutputStream fOut = null;

        File f1 = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/gyk301/Fotograflar");

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                .format(new Date());


        File file = new File(f1.getAbsolutePath()+"/"+timeStamp + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.w("SavePhoto", "saveImage: ", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("SavePhoto", "saveImage: ", e);
        }
    }

    public boolean checkPermission() {
        // result WRITE_EXTERNAL_STORAGE izni var mı? varsa 0 yoksa -1
        int result = ContextCompat.
                checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // result1 RECORD_AUDIO izni var mı? varsa 0 yoksa -1
        int result1 = ContextCompat.
                checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        //İkisinede izin verilmiş ise true diğer durumlarda false döner
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        //Verilen String[] dizisi içerisindeki izinlere istek atılır
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CAMERA_PERMISSON_REQUEST_CODE);
    }


    //İstek atılır istek onay/red işlemi bittiğinde bu metod çalışır
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode istek atılırken kullanılan kod ile aynıysa
        if (requestCode == CAMERA_PERMISSON_REQUEST_CODE) {
            if (grantResults.length > 0) { // İzin verilenlerin listesi en az 1 elemanlı ise
                //Record izni verildi mi?
                boolean permissionToCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                //External Store izni verildi mi
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                //izinler kontrol edilir
                if (permissionToCamera && permissionToStore) {
                    Toast.makeText(this, "İzinler alındı!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "İzin vermen gerekli!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
