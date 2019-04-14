package com.gyk.gyk301;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebSiteActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_site);
        webView = (WebView) findViewById(R.id.webView);

        String url = "https://gelecegiyazanlar.turkcell.com.tr/";
        openWebPage(url);
    }
    private void openWebPage(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        final ProgressDialog progressDialog =
                ProgressDialog.show(this,"Geleceği yazanlar",
                        "Yükleniyor...",true);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                Toast.makeText(WebSiteActivity.this, "Sayfa yüklendi!",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view,
                                        int errorCode, String description, String failingUrl) {
                Toast.makeText(WebSiteActivity.this, "Bir hata oluştu",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}
