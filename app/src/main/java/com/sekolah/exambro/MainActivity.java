package com.sekolah.exambro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    
    // SETTING PIN DI SINI (BISA LU UBAH KAPAN SAJA)
    private final String PIN_MASUK = "9999";
    private final String PIN_KELUAR = "12345678";
    
    private boolean isUnlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Anti Screenshot & Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        webView = new WebView(this);
        setContentView(webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Langsung munculkan dialog PIN Masuk sebelum memuat URL CBT
        showLoginPinDialog();
    }

    private void showLoginPinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Ujian Siswa");
        builder.setMessage("Masukkan PIN Masuk untuk memulai ujian:");
        builder.setCancelable(false);

        final EditText input = new EditText(this);
        input.setHint("PIN Masuk");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Masuk", (dialog, which) -> {
            String enteredPin = input.getText().toString();
            if (enteredPin.equals(PIN_MASUK)) {
                isUnlocked = true;
                // TEMBAK KE CBT SMP TUNAS HARAPAN
                webView.loadUrl("https://smptunasharapan.sch.id/cbt");
            } else {
                Toast.makeText(MainActivity.this, "PIN Masuk Salah!", Toast.LENGTH_SHORT).show();
                showLoginPinDialog(); // Ulangi kalau salah
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isUnlocked) {
                showExitPinDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitPinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keluar Aplikasi Ujian");
        builder.setMessage("Masukkan PIN Pengawas untuk keluar:");

        final EditText input = new EditText(this);
        input.setHint("PIN Keluar");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Keluar", (dialog, which) -> {
            String enteredPin = input.getText().toString();
            if (enteredPin.equals(PIN_KELUAR)) {
                finish();
            } else {
                Toast.makeText(MainActivity.this, "PIN Keluar Salah!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
