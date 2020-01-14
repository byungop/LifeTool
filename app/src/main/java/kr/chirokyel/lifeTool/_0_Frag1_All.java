package kr.chirokyel.lifeTool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class _0_Frag1_All extends AppCompatActivity {

    EditText frag1_main_edittext_memo; String memo;
    Button frag1_main_button_share;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;

    int fragCode; String lockCode = "";
    static String clearCode = "";
    Boolean moveStep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._0_frag1_main);

        loadData();

        // STEP 노티피케이션 누른 경우
        Intent stepIntent = getIntent();
        moveStep = stepIntent.getBooleanExtra("moveStep", false);
        int sto = stepIntent.getIntExtra("service", 0);
        System.out.println(sto);
        System.out.println(moveStep);
        if (moveStep) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            _0_Frag2_And_Weather_Step fragment2 = new _0_Frag2_And_Weather_Step();
            fragmentTransaction.replace(R.id.frame, fragment2);
            _0_Frag2_And_Weather_Step.fragmentCode1 = "스텝";
            fragmentTransaction.commit();

        } else if (lockCode.equals("잠금해제")) {

            // 인텐트 : 바로 로딩화면 시작
        } else {
            Intent loadingIntent = new Intent(_0_Frag1_All.this, _0_Frag0_Loading.class);
            startActivity(loadingIntent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 잠금해제 후 메인으로 들어올때는 항상 메모장 보이도록!
        if (clearCode.equals("잠금해제")) {
            clearStack();
            clearCode = "";
        }


        // 에딧텍스트 : 메모장 카톡 나에게 공유하기, 롱클릭
        frag1_main_edittext_memo = findViewById(R.id.fra1_main_edittext_memo);
        frag1_main_edittext_memo.setText(memo);


        frag1_main_button_share = findViewById(R.id.frag_main_button_share);
        frag1_main_button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(_0_Frag1_All.this);
                builder.setTitle("메모를 공유하시겠습니까?");
//                builder.setMessage("메모를 카카오톡에 보관하시겠습니까?");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String memoContents = frag1_main_edittext_memo.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, memoContents);
//                                intent.setPackage("com.kakao.talk");
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "보관완료!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        frag1_main_edittext_memo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(_0_Frag1_All.this);
                builder.setTitle("메모를 초기화 하시겠습니까?");
//                builder.setMessage("메모를 카카오톡에 보관하시겠습니까?");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                frag1_main_edittext_memo.setText("");
                                Toast.makeText(getApplicationContext(), "초기화완료!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return false;
            }
        });


        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                _1_Note_1_Note_3_Main_Fragment fragment1 = new _1_Note_1_Note_3_Main_Fragment();
                fragmentTransaction.replace(R.id.frame, fragment1);
                fragmentTransaction.commit();
                fragCode = 1;
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                _0_Frag2_And_Weather_Step fragment2 = new _0_Frag2_And_Weather_Step();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.commit();
                fragCode = 2;
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                _0_Frag3_And_Plan_Todo fragment3 = new _0_Frag3_And_Plan_Todo();
                fragmentTransaction.replace(R.id.frame, fragment3);
                fragmentTransaction.commit();
                fragCode = 3;
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                _0_Frag4_Setting fragment4 = new _0_Frag4_Setting();
                fragmentTransaction.replace(R.id.frame, fragment4);
                fragmentTransaction.commit();
                fragCode = 4;
            }
        });


        // 버튼 : 프레그먼트 모두 사라지게 만들기
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStack();
            }
        });
    }



    @Override
    public void onStop() {
        super.onStop();

        // 메서드 : 데이터 저장하기
        saveData();

        // 인텐트 : 위젯 포지션 받기
        Intent i =  getIntent();

        // 인텐트 : 위젯 내용 업데이트하기
        Intent intent = new Intent(_0_Frag1_All.this, _1_Note_4_Widget_1_Main.class);
        intent.setAction("update");
        intent.putExtra("position", i.getIntExtra("position", 0));
        sendBroadcast(intent);
    }



    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        memo = frag1_main_edittext_memo.getText().toString();
        editor.putString("memo", memo);
        editor.apply();
    }

    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        memo = sharedPreferences.getString("memo", null);

        this.lockCode = sharedPreferences.getString("lockCode", null);

        if (lockCode == null) {
            lockCode = "";
        }
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

}
