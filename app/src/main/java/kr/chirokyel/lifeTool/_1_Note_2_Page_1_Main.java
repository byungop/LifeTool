package kr.chirokyel.lifeTool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class _1_Note_2_Page_1_Main extends AppCompatActivity {

    private int noteKey;
    private int pageKey;

    private String noteSubject;

    DrawerLayout page_main_drawerlayout;
    TextView page_main_textview;
    Button page_main_button;
    EditText page_main_edittext;


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // pageslide pageslide pageslide pageslide pageslide pageslide pageslide pageslide pageslide
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    List<_1_Note_2_Page_2_Leftslide_1_Form> pageSubjectList;
    _1_Note_2_Page_2_Leftslide_2_Adapter page_leftslide_adapter;
    RecyclerView page_leftslide_itemlayout_recyclerview;


    Button page_main_leftslide_button;
    EditText page_leftslide_edittext;


    int selectAll = 0;
    Button page_leftslide_button_visible;
    Button page_leftslide_button_delete;
    Button page_leftslide_button_up;
    Button page_leftslide_button_down;
    Button leftslide_button_selectAll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._1_note_2_page_1_main);


        // 인텐트 : 노트키 받기
        Intent i = getIntent();
        noteKey = i.getIntExtra("noteKey", 0);
        noteSubject = i.getStringExtra("noteSubject");


        // 메서드 : 데이터 불러오기 (** 노트키를 먼저 받아와야 데이터를 불러올 수 있어 **)
        loadData();


        // 텍스트뷰 : 노트 제목 명시
        page_main_textview = findViewById(R.id.page_main_textview);
        page_main_textview.setText(noteSubject);


        // 버튼 : 드로어 열고닫기
        page_main_drawerlayout = findViewById(R.id.page_main_drawerlayout);
        page_main_button = findViewById(R.id.page_main_button);
        page_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_main_drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                    page_main_drawerlayout.closeDrawer(Gravity.LEFT);
                } else {
                    page_main_drawerlayout.openDrawer(Gravity.LEFT);
                }
            }
        });



        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // pageslide pageslide pageslide pageslide pageslide pageslide pageslide pageslide pageslide
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



        // 리사이클러뷰
        page_leftslide_itemlayout_recyclerview = findViewById(R.id.page_leftslide_recyclerView);
        page_leftslide_adapter = new _1_Note_2_Page_2_Leftslide_2_Adapter(pageSubjectList, this);
        page_leftslide_itemlayout_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        page_leftslide_itemlayout_recyclerview.setAdapter(page_leftslide_adapter);


        // 메서드 : 아이템 수정 삭제(1/3) ~ 드래그, 드랍, 스와이프
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        page_leftslide_itemlayout_recyclerview.addItemDecoration(dividerItemDecoration);


        // 에팃텍스트 : 리사이클러뷰 검색기능
        page_leftslide_edittext = findViewById(R.id.page_leftslide_edittext);
        page_leftslide_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                page_leftslide_adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // 버튼 : 새페이지 추가하기
        page_main_leftslide_button = findViewById(R.id.page_leftslide_button);
        page_main_leftslide_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _1_Note_2_Page_2_Leftslide_1_Form addpage = new _1_Note_2_Page_2_Leftslide_1_Form("새페이지", noteKey, pageKey, false, false);
                _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.add(addpage);
                pageKey++;

                // for consistent addpage's checkable attribute
                if (_1_Note_2_Page_2_Leftslide_2_Adapter.visibility == View.VISIBLE) {
                    addpage.setStateVisible(true);
                } else {
                    addpage.setStateVisible(false);
                }

                page_leftslide_adapter.getFilter().filter("");
                page_leftslide_adapter.notifyDataSetChanged();
            }
        });


        // 버튼 : 페이지 아이템 체크박스 보이게 안보이게
        page_leftslide_button_visible = findViewById(R.id.page_leftslide_button_visible);
        leftslide_button_selectAll = findViewById(R.id.page_leftslide_button_selectAll);
        page_leftslide_button_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // 체크박스 안보이게
                if (_1_Note_2_Page_2_Leftslide_2_Adapter.visibility == View.VISIBLE) {
                    for (int a = 0; a < pageSubjectList.size(); a++) {
                        pageSubjectList.get(a).setStateVisible(false);
                        pageSubjectList.get(a).setStateChecked(false);
                        page_leftslide_adapter.notifyDataSetChanged();
                    }
                    leftslide_button_selectAll.setVisibility(View.INVISIBLE);

                    // 체크박스 보이게
                } else  {
                    for (int a = 0; a < pageSubjectList.size(); a++) {
                        pageSubjectList.get(a).setStateVisible(true);
                        page_leftslide_adapter.notifyDataSetChanged();
                    }
                    leftslide_button_selectAll.setVisibility(View.VISIBLE);
                }
            }
        });


        // 버튼 : 페이지 삭제
        page_leftslide_button_delete = findViewById(R.id.page_leftslide_button_delete);
        page_leftslide_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // 검색중에는 삭제되지 않도록
                if (pageSubjectList.size() != _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.size()) {
                    Toast.makeText(getApplicationContext(), "검색 중에는 삭제할 수 없습니다", Toast.LENGTH_SHORT).show();

                    // 아이템 삭제는 역순으로!
                } else {
                    int a = _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.size();
                    for (int b = a - 1; b >= 0; b--) {
                        if (pageSubjectList.get(b).isStateChecked()) {
                            pageSubjectList.remove(b);
                            page_leftslide_adapter.notifyItemRemoved(b);
                        }
                    }
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.clear();
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.addAll(pageSubjectList);
                }

                // 삭제후 체크박스 보이지 않도록
                leftslide_button_selectAll.setVisibility(View.INVISIBLE);
                selectAll = 0;
                page_leftslide_adapter.notifyDataSetChanged();
                for (int a = 0; a < pageSubjectList.size(); a++) {
                    pageSubjectList.get(a).setStateVisible(false);
                }
                _1_Note_2_Page_2_Leftslide_2_Adapter.visibility = View.INVISIBLE;
            }
        });


        // 버튼 선택 : 페이지 아이템 모두 선택
        leftslide_button_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // 모두 선택되도록
                if (selectAll == 0) {
                    for (int a = 0; a < pageSubjectList.size(); a++) {
                        pageSubjectList.get(a).setStateChecked(true);
                        page_leftslide_adapter.notifyDataSetChanged();
                    }
                    selectAll = 1;

                    // 모두 선택되지 않도록
                } else {
                    for (int a = 0; a < pageSubjectList.size(); a++) {
                        pageSubjectList.get(a).setStateChecked(false);
                        page_leftslide_adapter.notifyDataSetChanged();
                    }
                    selectAll = 0;
                }
            }
        });


        // 버튼 : 페이지 순서 위로
        page_leftslide_button_up = findViewById(R.id.page_leftslide_button_up);
        page_leftslide_button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 체크된것 고르기
                int a = _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.size();
                ArrayList<Integer> array = new ArrayList<>();
                for (int b = a - 1; b >= 0; b--) {
                    if (pageSubjectList.get(b).isStateChecked()) {
                        array.add(b);
                    }
                }

                // 아무것도 체크되지 않은 경우
                if (array.size() == 0) {
                    Toast.makeText(getApplicationContext(), "대상을 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    // 체크된 것의 순서가 맨 위인 경우
                    if (array.get(array.size()-1) == 0) {
                        Toast.makeText(getApplicationContext(), "처음.", Toast.LENGTH_SHORT).show();

                    // 체크된 것들 순서 모두 하나씩 위로
                    } else {
                        int c = array.size();
                        for (int b = c - 1; b >=0; b--) {
                            _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.add(array.get(b)-1,
                                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.get(array.get(b)));
                            _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.remove(array.get(b)+1);
                            page_leftslide_adapter.notifyDataSetChanged();
                        }
                    }
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.clear();
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.addAll(pageSubjectList);
                }
            }
        });


        // 버튼 : 페이지 순서 아래로
        page_leftslide_button_down = findViewById(R.id.page_leftslide_button_down);
        page_leftslide_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 체크된것 고르기
                int a = _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.size();
                ArrayList<Integer> array = new ArrayList<>();
                for (int b = a - 1; b >= 0; b--) {
                    if (pageSubjectList.get(b).isStateChecked()) {
                        array.add(b);
                    }
                }

                // 아무것도 체크되지 않은 경우
                if (array.size() == 0) {
                    Toast.makeText(getApplicationContext(), "대상을 선택해주세요", Toast.LENGTH_SHORT).show();

                    // 체크된 것의 순서가 맨 아래인 경우
                } else {
                    if (array.get(0) == pageSubjectList.size() - 1) {
                        Toast.makeText(getApplicationContext(), "끝.", Toast.LENGTH_SHORT).show();

                        // 체크된 것들 순서 모두 하나씩 아래로
                    } else {
                        for (int b = 0; b <= array.size() -1 ; b++) {
                            _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.add(array.get(b)+2,
                                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.get(array.get(b)));

                            _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.remove(array.get(b)-0); // fucking index ((p.p))
                            page_leftslide_adapter.notifyDataSetChanged();
                        }
                    }
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.clear();
                    _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectListAll.addAll(pageSubjectList);
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        // 체크박스 보이지 않게, 체크 해제되게
        for (int a = 0; a < pageSubjectList.size(); a++) {
            pageSubjectList.get(a).setStateVisible(false);
            pageSubjectList.get(a).setStateChecked(false);
            page_leftslide_adapter.notifyDataSetChanged();
        }
        saveData();
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String pageLIst = gson.toJson(this.pageSubjectList);
        editor.putString("note/" + noteKey + "/page", pageLIst);

        // 변수 저장하기
        editor.putInt("noteKey/" + noteKey + "/pageKey", pageKey);

        editor.putString("note/" + noteKey + "/main", page_main_edittext.getText().toString());
        editor.apply();
    }

    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String pageList = sharedPreferences.getString("note/" + noteKey + "/page", null);
        Type type = new TypeToken<ArrayList<_1_Note_2_Page_2_Leftslide_1_Form>>() {}.getType();
        this.pageSubjectList = gson.fromJson(pageList, type);
        if (this.pageSubjectList == null) {
            this.pageSubjectList = new ArrayList<>();}

        // 변수 불러오기
        int pageKey = sharedPreferences.getInt("noteKey/" + noteKey + "/pageKey", 0);
        this.pageKey = pageKey;

        page_main_edittext = findViewById(R.id.page_main_edittext);
        page_main_edittext.setText(sharedPreferences.getString("note/" + noteKey + "/main", null));
    }
}






