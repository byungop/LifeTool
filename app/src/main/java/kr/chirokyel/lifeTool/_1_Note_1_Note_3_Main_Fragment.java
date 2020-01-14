package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class _1_Note_1_Note_3_Main_Fragment extends Fragment {

    Context context;
    int noteKey;

    List<_1_Note_1_Note_1_Form> noteList;
    static _1_Note_1_Note_2_Adapter note_2_adapter;
    RecyclerView note_main_recyclerview;

    Button note_main_button;

    public _1_Note_1_Note_3_Main_Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 메서드 : 데이터 불러오기
        loadData();

        return inflater.inflate(R.layout._1_note_1_note_2_main_fragment____________________1, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 리사이클러뷰
        note_main_recyclerview = getActivity().findViewById(R.id.note_main_recyclerview);
        note_2_adapter = new _1_Note_1_Note_2_Adapter(noteList, getContext());
        note_main_recyclerview.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        note_main_recyclerview.setAdapter(note_2_adapter);



        // 버튼 : 새노트 추가
        note_main_button = getActivity().findViewById(R.id.note_main_button_addnote);
        note_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_2_adapter.addNote(String.valueOf(Uri.parse("android.resource://kr.chirokyel.lifeTool/" + R.drawable._1_note_new)), "새노트", noteKey);
                noteKey ++;
                note_2_adapter.notifyDataSetChanged();
            }
        });


        // 메서드 : 아이템 수정 삭제(1/3) ~ 드래그, 드랍, 스와이프
        ItemTouchHelper itemTouchHelper_wallpaper = new ItemTouchHelper(note_simpleCallback);
        itemTouchHelper_wallpaper.attachToRecyclerView(note_main_recyclerview);

    }

    @Override
    public void onStop() {
        super.onStop();

        // 메서드 : 데이터 저장하기
        saveData();

        // 인텐트 : 위젯 포지션 받기
        Intent i =  getActivity().getIntent();

        // 인텐트 : 위젯 내용 업데이트하기
        Intent intent = new Intent(getActivity(), _1_Note_4_Widget_1_Main.class);
        intent.setAction("update");
        intent.putExtra("position", i.getIntExtra("position", 0));
        getActivity().sendBroadcast(intent);
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    // 메서드 : 아이템 위치변경(2/3) ~ 드래그
    ItemTouchHelper.SimpleCallback note_simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
            0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(_1_Note_1_Note_2_Adapter.noteList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
    };


    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String noteList = gson.toJson(this.noteList);
        editor.putString("note", noteList);

        // 변수 저장하기
        editor.putInt("noteKey", noteKey);
        editor.apply();
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String noteList = sharedPreferences.getString("note", null);
        Type type = new TypeToken<ArrayList<_1_Note_1_Note_1_Form>>() {}.getType();
        this.noteList = gson.fromJson(noteList, type);
        if (this.noteList == null) {
            this.noteList = new ArrayList<>();
        }

        // 변수 불러오기
        int noteKey  = sharedPreferences.getInt("noteKey", 0);
        this.noteKey = noteKey;
    }
}
