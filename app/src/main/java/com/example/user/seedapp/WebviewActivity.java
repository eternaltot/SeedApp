package com.example.user.seedapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;


public class WebviewActivity extends Activity {
    private WebView webView;
    private ImageButton bt_bk_web;
    private ImageView imageView;
    private String url="http://www.google.co.th";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        url = getIntent().getExtras().getString("URL");
        Log.d("Webview", url);
        webView = (WebView) findViewById(R.id.webView);
        bt_bk_web = (ImageButton) findViewById(R.id.back_bt_web);
        imageView = (ImageView) findViewById(R.id.imageView4);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                webView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        });

        bt_bk_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                WebviewActivity.this.overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
            }
        });
    }

    @Override // เหตุการณ์เมื่อกดปุ่ม เพิ่ม-ลด ด้านข้าง
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            WebviewActivity.this.overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
