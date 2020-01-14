package kr.chirokyel.lifeTool;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;

public class _5_Todo_4_Alram_1_Popup extends Activity {

    int position;

    TimePicker timePicker;
    Calendar calendar; int hour, min;
    String settingTime ="";
    static int changeCode = 0;

    Button button_setting;
    Button button_cancel;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._5_todo_3_alarm);


        // 인텐트 : 아이템 위치값 받기
        Intent i = getIntent();
        position = i.getIntExtra("position", 0);


        // 타임피커 : 알람시간 선택
        timePicker = findViewById(R.id.todo_alram_timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hour = hourOfDay;
                min = minute;
                changeCode = 1;

                // 캘린터 객체에 알람시간 집어넣기
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);
                settingTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
            }
        });



        // 버튼 : 알람설정
        button_setting = findViewById(R.id.todo_alram_button_setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // if timePicker no change
                if (changeCode == 0) {
                    Toast.makeText(getApplicationContext(), "시간을 변경해주세요", Toast.LENGTH_SHORT).show();

//                    // if timePicker change
                } else {
                    Toast.makeText(getApplicationContext(), "알람설정!", Toast.LENGTH_SHORT).show();
                    startAlarm(calendar);
                    _0_Frag3_And_Plan_Todo.fragmentCode2 = "투두";
                    changeCode = 0;
                    finish();
                }
            }
        });


        // 버튼 : 알람해제
        button_cancel = findViewById(R.id.todo_alram_button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "알람해제!", Toast.LENGTH_SHORT).show();
                cancelAlarm();
                _0_Frag3_And_Plan_Todo.fragmentCode2 = "투두";
                changeCode = 0;
                finish();
            }
        });
    }



    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // 메서드 : 알람설정
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {

        // 알람매니저 생성
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 펜딩인텐트 : 리시버 전달
        Intent intent = new Intent(this, _5_Todo_4_Alram_2_Receiver.class);
        intent.putExtra("position", position);
        Gson gson = new Gson();
        String todoList = gson.toJson(_5_Todo_2_Adapter.todolist);
        intent.putExtra("todoList", todoList);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                _5_Todo_2_Adapter.todolist.get(position).getTodoKey(), intent, 0);

        // 알람설정 확정
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        // 알람정보 보이게
        _5_Todo_2_Adapter.todolist.get(position).setStateVisible(true);
        _5_Todo_2_Adapter.todolist.get(position).setSettingTime(settingTime);
        _5_Todo_3_Main_Fragment.todo_adapter.notifyDataSetChanged();
    }


    // 메서드 : 알람해제
    private void cancelAlarm() {

        // 알람매니저 생성
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 펜딩인텐트 : 리시버 전달
        Intent intent = new Intent(this, _5_Todo_4_Alram_2_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                _5_Todo_2_Adapter.todolist.get(position).getTodoKey(), intent, 0);
        alarmManager.cancel(pendingIntent);

        // 알람정보 지우기
        _5_Todo_2_Adapter.todolist.get(position).setStateVisible(false);
        _5_Todo_2_Adapter.todolist.get(position).setSettingTime(null);
        _5_Todo_3_Main_Fragment.todo_adapter.notifyDataSetChanged();
    }
}
