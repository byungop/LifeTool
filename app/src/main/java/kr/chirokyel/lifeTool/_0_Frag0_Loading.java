package kr.chirokyel.lifeTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class _0_Frag0_Loading extends AppCompatActivity {

    String lockCode = "";

    TextView frag0_loading_textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._0_frag0_loading);
        loadData();
        startLoading();

        frag0_loading_textview = findViewById(R.id.frag0_loading_textview);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                frag0_loading_textview.setText("Life Tool");
            }
        }, 320);
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lockCode.equals("잠금설정")) {
                    Intent intent = new Intent(_0_Frag0_Loading.this, _7_Lock_2_Unlock.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        }, 4000);
    }

    private void loadData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        this.lockCode = sharedPreferences.getString("lockCode", null);

        if (lockCode == null) {
            lockCode = "";
        }
    }
}
