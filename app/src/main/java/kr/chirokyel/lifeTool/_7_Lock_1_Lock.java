package kr.chirokyel.lifeTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class _7_Lock_1_Lock extends AppCompatActivity {

    String password;
    String lockCode;

    EditText lock_lcok_edittext_password1;
    EditText lock_lcok_edittext_password2;
    TextView lock_lock_textview_passtest;
    Button lock_lock_button_confirm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._7_lock_1_lock);

        loadData();

        lock_lcok_edittext_password1 = findViewById(R.id.lock_lock_edittext_password1);
        lock_lcok_edittext_password2 = findViewById(R.id.lock_lock_edittext_password2);
        lock_lock_textview_passtest = findViewById(R.id.lock_lock_textview_passtest);
        lock_lock_button_confirm = findViewById(R.id.lock_lock_button_confirm);

        lock_lcok_edittext_password2.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lock_lcok_edittext_password1.getText().toString().equals(
                        lock_lcok_edittext_password2.getText().toString()
                )) {
                    lock_lock_textview_passtest.setText("비밀번호 일치");
                } else {
                    lock_lock_textview_passtest.setText("비밀번호 불일치");
                }
            }
        });


        // 버튼 : 잠금모드 설정하기
        lock_lock_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 비밀번호 일치하는 경우
                if (lock_lock_textview_passtest.getText().toString().equals("비밀번호 일치")) {
                    Toast.makeText(getApplicationContext(), "잠금설정 완료!", Toast.LENGTH_SHORT).show();
                    lockCode = "잠금설정";
                    password = lock_lcok_edittext_password1.getText().toString();
                    saveData();
                    Intent intent = new Intent(getApplicationContext(), _0_Frag0_Loading.class);
                    startActivity(intent);
                    finish();

                // 비밀번호 일치하지 않는 경우
                } else {
                    Toast.makeText(getApplicationContext(), "다시 확인해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // save data
    private void saveData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("password", password);
        editor.putString("lockCode", lockCode);
        editor.apply();
    }


    private void loadData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        this.password = sharedPreferences.getString("password", null);
        this.lockCode = sharedPreferences.getString("lockCode", null);
    }



}
