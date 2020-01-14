package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class _0_Frag4_Setting extends Fragment {

    String password = "";
    String lockCode = "";
    Button frag4_setting_main_lock;
    Button frag4_setting_main_music;

    public _0_Frag4_Setting() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout._0_frag4_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        frag4_setting_main_lock = getActivity().findViewById(R.id.frag4_main_button_lock);
        frag4_setting_main_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadData();

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("비밀번호를 입력하세요 (4자리, 숫자)");
                final EditText contents1 = new EditText(getContext());
                contents1.setText("");
                contents1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                alert.setView(contents1);

                // - confirm : button
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (contents1.getText().toString().equals(password)) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), _7_Lock_1_Lock.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // - cancel : button
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                if (lockCode.equals("잠금설정")) {
                    // - unlock : button
                    alert.setNeutralButton("잠금해제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (contents1.getText().toString().equals(password)) {
                                lockCode = "잠금해제";
                                saveData();
                                Toast.makeText(getContext(), "잠금모드 해제", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }
                    });
                }

                alert.show();
            }
        });



        frag4_setting_main_music = getActivity().findViewById(R.id.frag4_main_button_music);
        frag4_setting_main_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), _6_Music_1_Music_Player.class);
                startActivity(intent);
            }
        });


    }


    private void saveData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("password", password);
        editor.putString("lockCode", lockCode);
        editor.apply();
    }

    private void loadData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        this.password = sharedPreferences.getString("password", null);
        this.lockCode = sharedPreferences.getString("lockCode", null);
        if (password == null) {
            password = "";
        }
        if (lockCode == null) {
            lockCode = "";
        }
    }
}
