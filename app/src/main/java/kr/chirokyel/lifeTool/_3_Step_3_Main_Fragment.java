package kr.chirokyel.lifeTool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class _3_Step_3_Main_Fragment extends Fragment {

    private View view;

    Button step_main_button_test;
    Switch step_main_switch;
    TextView step_textview_step;
    TextView step_textview_distance;
    Button step_main_button_record;

    List<_3_Step_1_Form> stepList;
    static _3_Step_2_Adapter step_adapter;
    RecyclerView step_main_recyclerView;

    _3_Step_4_Service step_service;
    boolean isbound = false;
    boolean ischecked;

    Handler handler;
    Runnable runnable;

    public static _3_Step_3_Main_Fragment newInstance() {
        _3_Step_3_Main_Fragment fragTuesday = new _3_Step_3_Main_Fragment();
        return fragTuesday;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout._3_step_2_main_fragment____________________3, container, false);
        System.out.println("onCreate3");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 메서드 : 데이터 불러오기
        loadData();


        // 바인드 : 서비스 바인드(1/2)
        Intent intent = new Intent(getActivity().getApplicationContext(), _3_Step_4_Service.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);


        // 버튼 : 테스트용. 하루 더하기
        step_main_button_test = getActivity().findViewById(R.id.step_main_button_test);
        step_main_button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step_service.setDay();
            }
        });


        // 스위치 : 노티피케이션 온오프
        step_main_switch = getActivity().findViewById(R.id.step_main_switch);
        if (ischecked) {step_main_switch.setChecked(true);
        } else {step_main_switch.setChecked(false);
        }
        step_main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                step_service.notificationVisible(isChecked);
                ischecked = isChecked;
            }
        });


        step_textview_step = getActivity().findViewById(R.id.step_main_textview_step);
        step_textview_distance = getActivity().findViewById(R.id.step_main_textview_distance);


        // 버튼 : 기록하기
        step_main_button_record = getActivity().findViewById(R.id.step_main_button_record);
        step_main_button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step_adapter.addStep(step_service.getDay1(),
                        Integer.toString(step_service.getStepCountAfter()) + " 걸음",
                        String.format("%.2f", step_service.getStepCountAfter() * 0.0003) + " km");
                step_adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "기록!", Toast.LENGTH_SHORT).show();
            }
        });


        // 리사이클러뷰 : 세팅
        step_main_recyclerView = getActivity().findViewById(R.id.step_main_recyclerview);
        step_adapter = new _3_Step_2_Adapter(stepList, getContext());
        step_main_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        step_main_recyclerView.setAdapter(step_adapter);


        // 메서드 : 걸음, 거리 업데이트(1/2)
        runStep();
        System.out.println("onResume3");
    }

    @Override
    public void onStop() {
        super.onStop();
        // 메서드 : 데이터 저장하기
        saveData();
        System.out.println("onStop3");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 바인드 : 해제
        if (isbound) {
            getActivity().unbindService(serviceConnection);
            isbound = false;
        }
        System.out.println("onDestroy3");
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // 바인드 : 서비스 바인드(2/2)
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override

        // 바인드 연결
        public void onServiceConnected(ComponentName name, IBinder service) {
            _3_Step_4_Service.StepBinder stepBinder = (_3_Step_4_Service.StepBinder) service;
            step_service = (_3_Step_4_Service) stepBinder.getService();
            isbound = true;
        }

        // 바인드 해제
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isbound = false;
        }
    };



    // 메서드 : 데이터 저장하기
    private void saveData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String stepList = gson.toJson(this.stepList);
        editor.putString("steplist", stepList);

        // 변수 저장하기
        editor.putBoolean("ischecked", ischecked);
        editor.apply();
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String stepList = sharedPreferences.getString("steplist", null);
        Type type = new TypeToken<ArrayList<_3_Step_1_Form>>() {}.getType();
        this.stepList = gson.fromJson(stepList, type);
        if (this.stepList == null) {
            this.stepList = new ArrayList<>();}

        // 변수 불러오기
        this.ischecked = sharedPreferences.getBoolean("ischecked", false);
    }


    // 메서드 : 걸음, 거리 업데이트(2/2)
    public void runStep() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 500);
                    step_textview_step.setText(Integer.toString(step_service.getStepCountAfter()) + " 걸음");
                    step_textview_distance.setText(String.format("%.2f", step_service.getStepCountAfter() * 0.0003) + " km");
                } catch (NullPointerException e) {
                }
            }
        };
        runnable.run();
    }
}
