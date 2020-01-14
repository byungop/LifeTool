package kr.chirokyel.lifeTool;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class _5_Todo_4_Alram_3_Notification extends ContextWrapper {

    int position;
    ArrayList<_5_Todo_1_Form> todoList;
    _5_Todo_2_Adapter todo_2_adapter;

    private NotificationManager notificationManager;


    // 노티피케이션 생성자
    public _5_Todo_4_Alram_3_Notification(Context base, int position, ArrayList<_5_Todo_1_Form> todoList) {
        super(base);

        // 버전에 따라 채널 생성 여부 결정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        this.position = position;
        this.todoList = todoList;
        }

    // 메서드 : 노티피케이션 메니저 생성
    public NotificationManager notificationManager() {

        // 노티피케이션 서비스와 연결
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // 쉐어드로 알람정보 사용하기, 쉐어드로 안오면 생성자로 받은 정보 사용하기
        loadData();
        if (_5_Todo_2_Adapter.todolist != null && _5_Todo_3_Main_Fragment.todo_adapter !=null) {
            _5_Todo_2_Adapter.todolist.get(position).setStateVisible(false);
            _5_Todo_2_Adapter.todolist.get(position).setSettingTime("");
            _5_Todo_3_Main_Fragment.todo_adapter.notifyDataSetChanged();
        } else {
            todo_2_adapter = new _5_Todo_2_Adapter(todoList);
            todoList.get(position).setStateVisible(false);
            todoList.get(position).setSettingTime("");
        }
        saveData();

        return notificationManager;
    }


    // 메서드 : 노티피케이션 꾸미기
    public NotificationCompat.Builder getChannelNotification() {
        long[] vibratePattern = {10000, 10000, 10000};
        return new NotificationCompat.Builder(getApplicationContext(), "channelID")
                .setContentTitle(todoList.get(position).getTodo())
                .setContentText("알람이 왔어요!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setVibrate(vibratePattern);
    }


    // 메서드 : 노티피케이션 체널 생성
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel("channelID", "Channel Name", NotificationManager.IMPORTANCE_HIGH);
        notificationManager().createNotificationChannel(channel);
    }



    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String todolist = gson.toJson(this.todoList);
        editor.putString("todo", todolist);
        editor.apply();
    }

    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String todolist = sharedPreferences.getString("todo", null);
        Type type = new TypeToken<ArrayList<_5_Todo_1_Form>>() {}.getType();
        this.todoList = gson.fromJson(todolist, type);
        if (this.todoList == null) {
            this.todoList = new ArrayList<>();
        }
    }
}