package kr.chirokyel.lifeTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class _7_Lock_2_Unlock extends AppCompatActivity {

    String password = "";
    EditText lock_unlock_edittext_password;
    Button lock_unlock_button_confirm;
    Button lock_unlock_button_eidt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._7_lock_2_unlock);

        loadData();



        lock_unlock_edittext_password = findViewById(R.id.lock_unlock_edittext_password);
        lock_unlock_button_confirm = findViewById(R.id.lock_unlock_button_confirm);
        lock_unlock_button_eidt = findViewById(R.id.lock_unlock_button_edit);

        lock_unlock_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // 비밀번호 일치하는 경우
                if (lock_unlock_edittext_password.getText().toString().equals(password)) {
                    Intent unlockIntent = new Intent(_7_Lock_2_Unlock.this, _7_Lock_3_Animation.class);
                    startActivity(unlockIntent);

                    _0_Frag1_All.clearCode = "잠금해제";


                    // 잠금해제 애니메이션 속도에 맞춰서 잠금해제 화면도 사라지게 만들기
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {finish();
                        }
                    }, 4500);

                    // 비밀번호 불일치하는 경우
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    lock_unlock_edittext_password.setText("");
                }
            }
        });


        lock_unlock_button_eidt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock_unlock_edittext_password.setText("");
            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    private void loadData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        this.password = sharedPreferences.getString("password", null);
        if (password == null) {
            password = "";
        }
    }

}
