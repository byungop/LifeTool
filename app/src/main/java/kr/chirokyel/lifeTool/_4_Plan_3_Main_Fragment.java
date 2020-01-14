package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class _4_Plan_3_Main_Fragment extends Fragment {

    private View view; static String planCode = "";

    static ArrayList<_4_Plan_1_Form> planlist;
    static _4_Plan_2_Adatper plan_adatper;
    private RecyclerView plan_main_recyclerview;
    private LinearLayoutManager linearLayoutManager;

    // 계획 변수
    Button plan_main_button_addplan;

    // 카운터 변수
    int count = 0;
    Button plan_main_button_plus;
    Button plan_main_button_counter;
    Button todo_main_button_minus;

    public static _4_Plan_3_Main_Fragment newInstance() {
        _4_Plan_3_Main_Fragment fragMonday = new _4_Plan_3_Main_Fragment();
        return fragMonday;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout._4_plan_2_main_fragment____________________4, container, false);

        Toast.makeText(getContext(), planCode, Toast.LENGTH_SHORT).show();
        // 메서드 : 데이터 불러오기
        if (planCode.equals("컬러")) {
            saveData();
        } else {
            loadData();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        // 리사이클러뷰 : 생성
        plan_main_recyclerview = getActivity().findViewById(R.id.plan_main_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        plan_main_recyclerview.setLayoutManager(linearLayoutManager);
        plan_adatper = new _4_Plan_2_Adatper(planlist);
        plan_main_recyclerview.setAdapter(plan_adatper);

        // 버튼 : 할일추가
        plan_main_button_addplan = getActivity().findViewById(R.id.plan_main_button_addplan);
        plan_main_button_addplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan_adatper.addplan("00:00", "00:00", "새로운 계획", "0");
                plan_adatper.notifyDataSetChanged();
            }
        });

        // 메서드 : 아이템 수정 삭제(1/3) ~ 드래그, 드랍, 스와이프
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(plan_main_recyclerview);


        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // plan & counter & plan & counter & plan & counter & plan & counter & plan & counter &
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        // 버튼 : 카운터 입력
        plan_main_button_counter = getActivity().findViewById(R.id.plan_main_button_counter);
        plan_main_button_counter.setText(Integer.toString(count));

        // 버튼 : 카운터 초기화
        plan_main_button_counter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                count = 0;
                plan_main_button_counter.setText(Integer.toString(count));
                return true;
            }
        });

        // 버튼 : 카운터 1 증가
        plan_main_button_plus = getActivity().findViewById(R.id.plan_main_button_plus);
        plan_main_button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                plan_main_button_counter.setText(Integer.toString(count));
            }
        });

        // 버튼 : 카운터 2 증가
        plan_main_button_plus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                count += 2;
                plan_main_button_counter.setText(Integer.toString(count));
                return false;
            }
        });

        // 버튼 : 카운터 1 감소
        todo_main_button_minus = getActivity().findViewById(R.id.plan_main_button_minus);
        todo_main_button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 0) {
                    Toast.makeText(getContext(), "그만!", Toast.LENGTH_SHORT).show();
                } else {
                    count -= 1;
                    plan_main_button_counter.setText(Integer.toString(count));
                }
            }
        });

        // 버튼 : 카운터 2 감소
        todo_main_button_minus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (count <= 1) {
                    Toast.makeText(getContext(), "그만!", Toast.LENGTH_SHORT).show();
                } else {
                    count -= 2;
                    plan_main_button_counter.setText(Integer.toString(count));
                }
                return true;
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




    // 메서드 : 아이템 수정 삭제(2/3) ~ 드래그, 드랍, 스와이프

    _4_Plan_1_Form deleted = null;
    String edited = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        // 아이템 순서바꾸기 ~ 드래그 앤 드랍
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(planlist, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        // 아이템 수정삭제 ~ 스와이프
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch (direction) {

                // 왼쪽 스와이프 : 아이템 삭제
                case ItemTouchHelper.LEFT:
                    deleted = planlist.get(position);
                    planlist.remove(position);
                    plan_adatper.notifyItemRemoved(position);
                    Snackbar.make(plan_main_recyclerview, deleted.getPlan(), Snackbar.LENGTH_LONG)
                            .setAction("되살리기", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    planlist.add(position, deleted);
                                    plan_adatper.notifyItemInserted(position);
                                }
                            }).show();
                    break;

                // 오른쪽 스와이프 : 아이템 수정
                case ItemTouchHelper.RIGHT:
                    Intent i = new Intent(getContext(), _4_Plan_4_Popup.class);
                    i.putExtra("position", position);
                    startActivity(i);
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


    // 메서드 데이터 저장하기
    private void saveData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String planlist = gson.toJson(_4_Plan_3_Main_Fragment.planlist);
        editor.putString("plan_list", planlist);

        // 변수 저장하기
        editor.putInt("plan_count", count);
        editor.apply();
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String json = sharedPreferences.getString("plan_list", null);
        Type type = new TypeToken<ArrayList<_4_Plan_1_Form>>() {}.getType();
        planlist = gson.fromJson(json, type);
        if (planlist == null) {
            planlist = new ArrayList<>();
        }

        // 변수 불러오기
        count = sharedPreferences.getInt("plan_count", 0);
    }
}