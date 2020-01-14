package kr.chirokyel.lifeTool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class _5_Todo_3_Main_Fragment extends Fragment {

    private View view;

    // recyclerView
    private ArrayList<_5_Todo_1_Form> todolist;
    static _5_Todo_2_Adapter todo_adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    Button todo_main_button_addtodo;

    // imprint unique code on item
    int todoKey = 0;

    public static _5_Todo_3_Main_Fragment newInstance() {
        _5_Todo_3_Main_Fragment Thuesday = new _5_Todo_3_Main_Fragment();
        return Thuesday;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout._5_todo_2_main_fragment____________________5, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 메서드 : 데이터 불러오기
        loadData();


        // 리사이클러뷰
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.todo_main_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        todo_adapter = new _5_Todo_2_Adapter(todolist);
        recyclerView.setAdapter(todo_adapter);


        // 버튼 : 할일 추가
        todo_main_button_addtodo = getActivity().findViewById(R.id.todo_main_button_addtodo);
        todo_main_button_addtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo_adapter.addtodo("새로운 할일", todoKey);
                todoKey ++;
                todo_adapter.notifyDataSetChanged();
            }
        });


        // 메서드 : 아이템 수정 삭제(1/3) ~ 드래그, 드랍, 스와이프
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStop() {
        super.onStop();

        // 메서드 : 데이터 저장하기
        saveData();
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // 메서드 : 아이템 수정 삭제(2/3) ~ 드래그, 드랍, 스와이프

    _5_Todo_1_Form deleted = null;
    String edited = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


        // 아이템 순서바꾸기 ~ 드래그 앤 드랍
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(todolist, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }


        // 아이템 수정삭제 ~ 스와이프
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {

                // 왼쪽 스와이프 : 아이템 삭제, 알람해제
                case ItemTouchHelper.LEFT:
                    if (todolist.get(position).getSettingTime() != null) {
                        cancelAlarm(position);
                        Toast.makeText(getContext(), "알람해제!", Toast.LENGTH_SHORT).show();
                    }

                    deleted = todolist.get(position);
                    todolist.remove(position);
                    todo_adapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deleted.getTodo(), Snackbar.LENGTH_LONG)
                            .setAction("원상태로", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    todolist.add(position, deleted);
                                    todo_adapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;


                // 오른쪽 스와이프 : 아이템 수정
                case ItemTouchHelper.RIGHT:
                    edited = todolist.get(position).getTodo();
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("할일을 수정하세요");
                    final EditText contents1 = new EditText(getContext());
                    contents1.setText(todolist.get(position).getTodo());
                    alert.setView(contents1);

                    // 버튼 : 수정하기
                    alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String contents2 = contents1.getText().toString();
                            todolist.get(position).setTodo(contents2);
                            todo_adapter.notifyDataSetChanged();
                            Snackbar.make(recyclerView, edited, Snackbar.LENGTH_LONG)
                                    .setAction("원상태로", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            todolist.get(position).setTodo(edited);
                                            todo_adapter.notifyDataSetChanged();
                                        }
                                    }).show();
                        }
                    });

                    // 버튼 : 취소하기
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    // 알람 : 알람 팝업으로 이동
                    alert.setNeutralButton("알람", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getContext(), _5_Todo_4_Alram_1_Popup.class);
                            i.putExtra("position", position);
                            startActivity(i);
                        }
                    });

                    todo_adapter.notifyDataSetChanged();
                    alert.show();
                    break;
            }
        }


        // 메서드 : 아이템 수정 삭제(3/3) ~ 꾸미기
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSwipeRed))
                    .addSwipeLeftLabel("DELETE")
                    .setSwipeLeftLabelColor(R.color.colorSwipeText)
                    .setSwipeLeftLabelTextSize(0, 20)
                    .addSwipeLeftActionIcon(R.drawable._0_basic_form_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSwipeBlue))
                    .addSwipeRightLabel("EDIT")
                    .setSwipeRightLabelColor(R.color.colorSwipeText)
                    .setSwipeRightLabelTextSize(0, 20)
                    .addSwipeRightActionIcon(R.drawable._0_basic_form_edit)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String todolist = gson.toJson(this.todolist);
        editor.putString("todo", todolist);

        // 변수 저장하기
        editor.putInt("todoKey", todoKey);
        editor.apply();
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String todolist = sharedPreferences.getString("todo", null);
        Type type = new TypeToken<ArrayList<_5_Todo_1_Form>>() {}.getType();
        this.todolist = gson.fromJson(todolist, type);
        if (this.todolist == null) {
            this.todolist = new ArrayList<>();
        }

        // 변수 불러오기
        int todokey = sharedPreferences.getInt("todoKey", 0);
        this.todoKey = todokey;

    }

    // 메서드 : 알람해제
    private void cancelAlarm(int position) {

        // 알람매니저 생성
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // 펜딩인텐트 : 리시버 전달
        Intent intent = new Intent(getContext(), _5_Todo_4_Alram_2_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                _5_Todo_2_Adapter.todolist.get(position).getTodoKey(), intent, 0);
        alarmManager.cancel(pendingIntent);

        // 알람정보 지우기
        _5_Todo_2_Adapter.todolist.get(position).setStateVisible(false);
        _5_Todo_2_Adapter.todolist.get(position).setSettingTime(null);
        _5_Todo_3_Main_Fragment.todo_adapter.notifyDataSetChanged();
    }
}