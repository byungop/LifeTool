package kr.chirokyel.lifeTool;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.List;

public class _3_Step_4_Service extends Service implements SensorEventListener {

    SensorManager sensorManager;
    boolean running = false;

    String day1;
    String day2;

    int noti;

    int stepCount;
    int stepCountBefore;
    int stepCountAfter;
    int rebootBackup;

    List<_3_Step_1_Form> stepList;
    _3_Step_2_Adapter step_adapter;

    IBinder iBinder = new StepBinder();

    private NotificationManagerCompat notificationManager;

    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_NAME_1 = "FIRSTCHANNEL";


    @Override
    public void onCreate() {
        super.onCreate();

        // 메서드 : 데이터 불러오기
        loadData();
        running = true;

        // 메서드 : 센서매니저 생성
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "센서를 찾을 수 없습니다!", Toast.LENGTH_SHORT).show();
        }

        // 캘린터 : 날짜 받아오기
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH) + 1;
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int dayNum = calendar.get(calendar.DAY_OF_WEEK);
        String dayName = "";

        switch (dayNum) {
            case 1 : dayName = "일요일";
                break;
            case 2 : dayName = "월요일";
                break;
            case 3 : dayName = "화요일";
                break;
            case 4 : dayName = "수요일";
                break;
            case 5 : dayName = "목요일";
                break;
            case 6 : dayName = "금요일";
                break;
            case 7 : dayName = "토요일";
                break;
        }

        day1 = year + " 년 " + month + " 월 " + day + " 일\n\n" + "*  " + dayName + "  *";

    }


    // 서비스 시작
    @SuppressLint("ServiceCast")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    // 서비스 종료
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        saveData();
    }


    // 메서드 : 걸음 감지 센서
    @Override
    public void onSensorChanged(SensorEvent event) {


        if (running) {

            // 센서 값 가져오기
            stepCount = Math.round(event.values[0]);

            // 앱 처음 설치한 경우
            if (day2 == null) {
                day2 = day1;
                // 센서 누적값 설정
                stepCountBefore = stepCount;
                Toast.makeText(getApplicationContext(), "값이 초기화되었습니다!", Toast.LENGTH_SHORT).show();


                // 디바이스 부팅한 경우
            } else if (stepCount < stepCountBefore) {
                rebootBackup = stepCountAfter;
                stepCountBefore = stepCount;
                Toast.makeText(getApplicationContext(), "값이 초기화되었습니다!", Toast.LENGTH_SHORT).show();


                // 하루 지난 경우
            } else if (!day1.equals(day2)) {

                // 어제 수치 자동 기록
                if (_3_Step_3_Main_Fragment.step_adapter != null) {
                    _3_Step_3_Main_Fragment.step_adapter.addStep(day2, Integer.toString(stepCount - stepCountBefore) + " 걸음",
                            String.format("%.2f", (stepCount - stepCountBefore) * 0.0003) + " km");
                    _3_Step_3_Main_Fragment.step_adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "쫀!", Toast.LENGTH_SHORT).show();
                }

                // 센서 누적값 설정
                stepCountBefore = stepCount;
                Toast.makeText(getApplicationContext(), "값이 초기화되었습니다!", Toast.LENGTH_SHORT).show();

                day2 = day1;
            }


            // 현재 걸음수 설정
            stepCountAfter = stepCount - stepCountBefore + rebootBackup;
            saveData();

            // 내가 이건 왜 넣었을까?
            if (noti == 1) {
                generateNotification();
            }
        }
    }


    // 바인드 생성 (1/2)
    public class StepBinder extends Binder {
        public _3_Step_4_Service getService() {
            return _3_Step_4_Service.this;
        }
    }

    // 바인드 생성 (2/2)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }





    // 바인드 메서드 : day1 보내기
    public String getDay1() {
        return day1;
    }

    // 바인드 메서드 : 현재 걸음수 보내기
    public int getStepCountAfter() {
        return stepCountAfter;
    }

    // 바인드 메서드 : 테스트용으로 날짜 하루 더하기
    public void setDay() {
        day1 += "1";
    }


    // 메서드 : 노티피케이션 보이게, 안보이게 ~ 스위치
    public void notificationVisible(boolean isChecked) {
        if (isChecked == true) {
            generateNotification();
            noti = 1;
        } else if (isChecked == false) {
            stopForeground(true);
            noti = 2;
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    // 메서드 : 데이터 저장하기
    private void saveData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 변수 저장하기
        editor.putString("day2", day2);
        editor.putInt("stepCountAfter", stepCountAfter);
        editor.putInt("stepCountBefore", stepCountBefore);
        editor.apply();
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // 변수 불러오기
        String day2 = sharedPreferences.getString("day2", null);
        this.day2 = day2;
        int stepCountAfter = sharedPreferences.getInt("stepCountAfter", 0);
        this.stepCountAfter = stepCountAfter;
        int stepCountBefore = sharedPreferences.getInt("stepCountBefore", 0);
        this.stepCountBefore = stepCountBefore;
    }


    // 메서드 : 노티피케이션 생성
    public void generateNotification() {

        notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel_01 = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel_01.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationChannel_01.enableLights(true);

            // - create notification manager
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel_01);
        }

        Intent touchintent = new Intent(this, _0_Frag1_All.class);
        touchintent.putExtra("moveStep", true);
        touchintent.putExtra("service" , 5);
        PendingIntent ptouchintent = PendingIntent.getActivity(this, 0, touchintent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification channel1 = new NotificationCompat.Builder(getApplicationContext(), "channel1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Step Counter")
                .setContentText(Integer.toString(stepCount - stepCountBefore) + " 걸음  /  " +
                        String.format("%.2f", (stepCount - stepCountBefore) * 0.0003) + " km")
                .setContentIntent(ptouchintent)
                .setOngoing(true)
                .setChannelId(CHANNEL_ID_1)
                .setWhen(0)
                .build();

        notificationManager.notify(1, channel1);
        startForeground(1, channel1);

    }
}
