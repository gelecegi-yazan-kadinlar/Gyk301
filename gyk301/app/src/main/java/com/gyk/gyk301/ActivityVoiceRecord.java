package com.gyk.gyk301;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityVoiceRecord extends AppCompatActivity implements View.OnClickListener{
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String filePath =
            Environment.getExternalStorageDirectory().getPath()+"/gyk301/Sesler/";
    private static final int REQUEST_AUIDO_PERMISSION_CODE = 200;
    private String lastFileName;
    private CustomVoiceListAdapter adapter;
    private List<String> fileNameList;
    private AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_record);

        fileNameList = new ArrayList<>();


        if(!checkPermission()) // Mikrofona ve Dosyalara erişime izin verilmemişse
         requestPermission(); // izin iste

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        Button buttonStartRecord = (Button) findViewById(R.id.buttonStartVoiceRecord);
        Button buttonStopRecord = (Button) findViewById(R.id.buttonStopVoiceRecord);
        Button buttonPlayVoice = (Button) findViewById(R.id.buttonPlayVoice);
        ListView listViewVoices = (ListView) findViewById(R.id.listViewVoices);
        buttonStartRecord.setOnClickListener(this);
        buttonStopRecord.setOnClickListener(this);
        buttonPlayVoice.setOnClickListener(this);
        if(createFolder()){
            getVoiceList();
        }
        try{
            getVoiceList();
        }catch (Exception e){
            Log.w("VoiceList", "onCreate: ",e );
        }

        fileNameList = new ArrayList<>();
        adapter = new CustomVoiceListAdapter(fileNameList,this);
        listViewVoices.setAdapter(adapter);
        listViewVoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startPlaying(fileNameList.get(position));
                Log.d("ListView", "onItemClick: "+fileNameList.get(position));
            }
        });

    }

    public void fillAutoCompleteTextView(final List<String> fileNameList){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, fileNameList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = fileNameList.get(i);
                Toast.makeText(ActivityVoiceRecord.this, selectedItem+" oynatılıyor.", Toast.LENGTH_SHORT).show();
                startPlaying(selectedItem);
            }
        });

    }
    public void startRecording(){


        try{ // Try-Catch  hata olması durumunda uygulamanın sonlandırılmasını engeller
            if (!createFolder()){
                return;
            }
            String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                    .format(new Date());
            lastFileName =timeStamp+".mp4";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//Ses kaydı alınacak akynak
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // ses kaydı formatı
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Araştır!
            mediaRecorder.setOutputFile(filePath+"/"+lastFileName); // Kaydedileceği yer
            mediaRecorder.prepare(); // hazırlıklar yukarıdaki seçeneklerle hazırlanması
            mediaRecorder.start(); // kayda başla
            fileNameList.add(lastFileName); //Listeye eleman ekleme
            adapter.notifyDataSetChanged();//Listviewı güncelleme
            Toast.makeText(this, "Kayıt başladı", Toast.LENGTH_SHORT).show();

        }catch (Exception exception){ // Exception en genel hata türü
            Log.w("Voice", "startRecording: ",exception );
        }
    }
    public void stopRecording(){
        if(mediaRecorder != null){ // mediaRecorder tanımlanmamışsa durdurulmaz
            Toast.makeText(this, "Kayıt durduruldu", Toast.LENGTH_SHORT).show();
            mediaRecorder.stop(); // Kaydı durdur
            mediaRecorder.reset(); // Kaydı başlatırken verdiğimiz seçenekleri sıfırla
            mediaRecorder.release(); // araştır!
            mediaRecorder = null; // kaydediciyi yoket

            adapter.notifyDataSetChanged();
        }
    }
    public boolean createFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "gyk301/Sesler"); //Klasör oluşturur

        if (!folder.exists()) { //Oluşturulduysa true döner
            return folder.mkdirs();
        }else{
            return true;
        }

    }
    public void startPlaying(String fileName){
        mediaPlayer = new MediaPlayer(); // Oynatıcıyı tanımla
        mediaPlayer.setVolume(1.0f,1.0f); // Sol sağ ses düzeyini ayarla
        try{
            mediaPlayer.setDataSource(filePath+"/"+fileName);//Hangi dosyayı oynatacağı
            mediaPlayer.prepare(); // seçeneklere göre ayarla
            mediaPlayer.start(); // Oynatmayı başlat
            Toast.makeText(this, "Oynatılıyor!", Toast.LENGTH_SHORT).show();

            //setOnCompletionLister tamamlanıp tamamlanmadığını dinler
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) { // Tamamlandığında çalıştırılır
                    mediaPlayer.stop();// Oynatıcı durdurulur
                    mediaPlayer.release();
                    mediaPlayer = null; // oynatıcı yokedilir
                    Toast.makeText(ActivityVoiceRecord.this, "Bitti", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.w("Playing", "startPlaying: ",e );
        }
    }
    public void getVoiceList(){
        fileNameList.clear(); //Listeyi temizler
        Log.d("Files", "Path: " + filePath); //log kaydı
        File directory = new File(filePath); // Klasör değişkeni
        File[] files = directory.listFiles(); //Dosyaları diziye atar
        Log.d("Files", "Size: "+ files.length); // Dosya sayısını log düşer
        for (int i = 0; i < files.length; i++)
        {
            fileNameList.add(files[i].getName()); //listeye eleman ekler
            Log.d("Files", "FileName:" + files[i].getName()); //dosya ismini loglar
        }
        fillAutoCompleteTextView(fileNameList);
    }
    public boolean checkPermission(){
        // result WRITE_EXTERNAL_STORAGE izni var mı? varsa 0 yoksa -1
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // result1 RECORD_AUDIO izni var mı? varsa 0 yoksa -1
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO);
        //İkisinede izin verilmiş ise true diğer durumlarda false döner
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    public void requestPermission(){
        //Verilen String[] dizisi içerisindeki izinlere istek atılır
        ActivityCompat.requestPermissions(ActivityVoiceRecord.this,
                new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_AUIDO_PERMISSION_CODE);
    }


    //İstek atılır istek onay/red işlemi bittiğinde bu metod çalışır
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode istek atılırken kullanılan kod ile aynıysa
        if(requestCode == REQUEST_AUIDO_PERMISSION_CODE){
            if(grantResults.length>0){ // İzin verilenlerin listesi en az 1 elemanlı ise
                //Record izni verildi mi?
                boolean permissionToRecord =grantResults[0] == PackageManager.PERMISSION_GRANTED;
                //External Store izni verildi mi
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                //izinler kontrol edilir
                if(permissionToRecord && permissionToStore){
                    Toast.makeText(this, "İzinler alındı!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "İzin vermen gerekli!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonStartVoiceRecord:
                if(checkPermission()) {//kaydetmek için izinleri kontrol et
                    startRecording();//kaydı başlat
                }
                break;
            case R.id.buttonStopVoiceRecord:
               stopRecording();//kaydı durdur
                break;
            case R.id.buttonPlayVoice:
                startPlaying(lastFileName); //son kaydedileni oynat
                break;
        }
    }
}



