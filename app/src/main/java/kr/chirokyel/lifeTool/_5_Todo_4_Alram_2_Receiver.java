package kr.chirokyel.lifeTool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class _5_Todo_4_Alram_2_Receiver extends BroadcastReceiver {

    int position;
    ArrayList<_5_Todo_1_Form> todoList;

    @Override
    public void onReceive(Context context, Intent intent) {

        // 인텐트 : 알람정보 받기
        position = intent.getIntExtra("position", 0);
        Gson gson = new Gson();
        String todolist = intent.getStringExtra("todoList");
        Type type = new TypeToken<ArrayList<_5_Todo_1_Form>>() {}.getType();
        this.todoList = gson.fromJson(todolist, type);
        if (this.todoList == null) {
            this.todoList = new ArrayList<>();
        }

        // 노티피케이션 생성
        _5_Todo_4_Alram_3_Notification notification = new _5_Todo_4_Alram_3_Notification(context, position, todoList);
        NotificationCompat.Builder builder = notification.getChannelNotification();
        notification.notificationManager().notify(
                todoList.get(position).getTodoKey(), builder.build());
    }
}