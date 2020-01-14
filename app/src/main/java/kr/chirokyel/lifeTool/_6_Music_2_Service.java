package kr.chirokyel.lifeTool;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class _6_Music_2_Service extends Service {


    MediaPlayer mMediaPlayer;
    IBinder iBinder = new MusicBinder();

    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_NAME_1 = "FIRSTCHANNEL";
    public static final String ACTION_BEFORE = "action_before";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_AFTER = "action_after";
    public static final String ACTION_CLOSE = "action_close";

    Uri uri;
    String musicSubject;
    int pausePosition;
    int changeCode;
    String service;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();

        // 데이터 불러오기
        loadData();

        // 미디어플레이어 생성
        try {
            mMediaPlayer = MediaPlayer.create(this, uri);
        } catch (NullPointerException e) {
        }

        // 노티피케이션 생성
        notificationManager = NotificationManagerCompat.from(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            // 펜딩인텐트1 : 서비스 스타트
        if (intent.getAction() == null) {
            changeNotification();
            mMediaPlayer.seekTo(pausePosition);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            saveData();

            // 펜딩인텐트2 : 10초 전으로
        } else if (intent.getAction().equals(ACTION_BEFORE)) {
            int position = mMediaPlayer.getCurrentPosition();
            if (position > 10000) {
                mMediaPlayer.seekTo(getCurrentPosition() - 10000);
            } else {
                Toast.makeText(getApplicationContext(), "10초 부족", Toast.LENGTH_SHORT).show();
            }

            // 펜딩인텐트3 : 음악 재생
        } else if (intent.getAction().equals(ACTION_PLAY)) {
            mMediaPlayer.start();
            changeNotification();

            // 펜딩인텐트4 : 음악 정지
        } else if (intent.getAction().equals(ACTION_PAUSE)) {
            mMediaPlayer.pause();
            pausePosition = mMediaPlayer.getCurrentPosition();
            generateNotification();

            // 펜딩인텐트5 : 10초 후로
        } else if (intent.getAction().equals(ACTION_AFTER)) {
            int position = mMediaPlayer.getCurrentPosition();
            if (position < getDuration() - 10000) {
                mMediaPlayer.seekTo(getCurrentPosition() + 10000);
            } else {
                Toast.makeText(getApplicationContext(), "10초 부족", Toast.LENGTH_SHORT).show();
            }

            // 펜딩인텐트6 : 음악 종료
        } else if (intent.getAction().equals(ACTION_CLOSE)) {
            mMediaPlayer.pause();
            pausePosition = mMediaPlayer.getCurrentPosition();
            saveData();
            stopForeground(true);
        }

        return START_NOT_STICKY;
    }


    // 디스트로이가 필요할까 고민해보자.
    @Override
    public void onDestroy() {
        super.onDestroy();

//        Toast.makeText(getApplicationContext(),"서비스 종료", Toast.LENGTH_SHORT).show();
//        pausePosition = mMediaPlayer.getCurrentPosition();
//        mMediaPlayer.stop();
//        mMediaPlayer.release();
        saveData();
    }


    // 바인더 생성(1/2)
    public class MusicBinder extends Binder {
        public _6_Music_2_Service getService() {
            return _6_Music_2_Service.this;
        }
    }

    // 바인더 생성(2/2)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    // 바인드 메서드 : 음악 길이 보내기
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }
    // 바인드 메서드 : 음악 현재위치 보내기
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }
    // 바인드 메서드 : 미디어 플레이어 상태 보내기
    public boolean getMediaPlayerState() {
        return mMediaPlayer.isPlaying();
    }

    // 바인드 메서드 : 미디어 플레이어 정지
    public void setMediaPlayerPause() {
        mMediaPlayer.pause();
        pausePosition = mMediaPlayer.getCurrentPosition();
        generateNotification();
        saveData();
    }
    // 바인드 메서드 : 식바 위치 받기
    public void setSeekbarPosition(int seekbarPosition) {
        mMediaPlayer.seekTo(seekbarPosition);
    }
    // 바인드 메서드 : 바뀐 음악 uri 받기
    public void setMusicUri(Uri uri, String musicSubject) {
        this.uri = uri;
        this.musicSubject = musicSubject;
    }



    // bug.. not service
    public void changeMusic() {
        if (mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
        }
        this.mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        this.mMediaPlayer.setLooping(true);
        this.mMediaPlayer.start();

        if (notificationManager == null) {
            notificationManager = NotificationManagerCompat.from(this);
        }
        changeNotification();
    }


    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 변수 저장하기
        try {
            editor.putString("musicSubject", musicSubject); // musicSubject
            editor.putString("musicURI", uri.toString()); // uri
            editor.putInt("pausePosition", pausePosition);  // pausePosition
        } catch (NullPointerException e) {
        }
        editor.apply();

    }

    // 메서드 : 데이터 불러오기
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadData() {

        // 쉐어드 생성
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // 변수 불러오기
        try {
            this.musicSubject = sharedPreferences.getString("musicSubject", null);
            this.uri = Uri.parse(sharedPreferences.getString("musicURI", null));
            this.pausePosition = sharedPreferences.getInt("pausePosition", 0);
        } catch (NullPointerException e) {
        }
    }


    // 메서드 : 노티피케이션 생성하기
    public void generateNotification() {

        // 펜딩인텐트1 : 10초 이전으로
        Intent beforeintent = new Intent(this, _6_Music_2_Service.class);
        beforeintent.setAction(ACTION_BEFORE);
        PendingIntent pbeforeintent = PendingIntent.getService(this, 0, beforeintent, 0);

        // 펜딩인텐트2 : 음악 재생
        Intent playintent = new Intent(this, _6_Music_2_Service.class);
        playintent.setAction(ACTION_PLAY);
        PendingIntent pplayintent = PendingIntent.getService(this, 0, playintent, 0);

        // 펜딩인텐트3 : 음악 정지
        Intent pauseintent = new Intent(this, _6_Music_2_Service.class);
        pauseintent.setAction(ACTION_PAUSE);
        PendingIntent ppauseintent = PendingIntent.getService(this, 0, pauseintent, 0);

        // 펜딩인텐트4 : 10초 이후로
        Intent afterintent = new Intent(this, _6_Music_2_Service.class);
        afterintent.setAction(ACTION_AFTER);
        PendingIntent pafterintent = PendingIntent.getService(this, 0, afterintent, 0);

        // 펜딩인텐트5 : 음악 종료
        Intent closeintent = new Intent(this, _6_Music_2_Service.class);
        closeintent.setAction(ACTION_CLOSE);
        PendingIntent pcloseintent = PendingIntent.getService(this, 0, closeintent, 0);

        // 펜딩인텐트6 : 노티피케이션 클릭하면 음악 액티비티로 화면전환
        Intent touchintent = new Intent(this, _6_Music_1_Music_Player.class);
        touchintent.putExtra("service" , 1);
        PendingIntent ptouchintent = PendingIntent.getActivity(this, 0, touchintent, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap largeImage = BitmapFactory.decodeResource(getResources(), R.drawable._6_music_largeicon);

        // 노티피케이션 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel_01 = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel_01.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationChannel_01.enableLights(true);
            notificationChannel_01.setLockscreenVisibility(0);

            // 노티피케이션 매니저 생성
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel_01);
        }

        // 노티피케이션 생성
        Notification channel1 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(musicSubject)
                .setLargeIcon(largeImage)
                .addAction(R.drawable._6_music_before, "like4", pbeforeintent)
                .addAction(R.drawable._6_music_play, "like1", pplayintent)
//                .addAction(R.drawable.music_pause, "like2", ppauseintent)
                .addAction(R.drawable._6_music_after, "like5", pafterintent)
                .addAction(R.drawable._6_music_close, "like3", pcloseintent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView()
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(pcloseintent))
                .setContentIntent(ptouchintent)
                .setOngoing(true)
                .build();

        // 일시정지 버튼 전환
        if (mMediaPlayer.isPlaying()) {
            changeNotification();
        }

        // 포어그라운드 서비스 실행
        notificationManager.notify(1, channel1);
        startForeground(1, channel1);
    }

    // 메서드 : 일시정지 노티피케이션
    public void changeNotification() {


        Intent beforeintent = new Intent(this, _6_Music_2_Service.class);
        beforeintent.setAction(ACTION_BEFORE);
        PendingIntent pbeforeintent = PendingIntent.getService(this, 0, beforeintent, 0);

        Intent playintent = new Intent(this, _6_Music_2_Service.class);
        playintent.setAction(ACTION_PLAY);
        PendingIntent pplayintent = PendingIntent.getService(this, 0, playintent, 0);

        Intent pauseintent = new Intent(this, _6_Music_2_Service.class);
        pauseintent.setAction(ACTION_PAUSE);
        PendingIntent ppauseintent = PendingIntent.getService(this, 0, pauseintent, 0);


        Intent afterintent = new Intent(this, _6_Music_2_Service.class);
        afterintent.setAction(ACTION_AFTER);
        PendingIntent pafterintent = PendingIntent.getService(this, 0, afterintent, 0);

        Intent closeintent = new Intent(this, _6_Music_2_Service.class);
        closeintent.setAction(ACTION_CLOSE);
        PendingIntent pcloseintent = PendingIntent.getService(this, 0, closeintent, 0);

        Intent touchintent = new Intent(this, _6_Music_1_Music_Player.class);
        touchintent.putExtra("service" , 1);
        PendingIntent ptouchintent = PendingIntent.getActivity(this, 0, touchintent, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap largeImage = BitmapFactory.decodeResource(getResources(), R.drawable._6_music_largeicon);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel_01 = new NotificationChannel(
                        CHANNEL_ID_1,
                        CHANNEL_NAME_1,
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel_01.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationChannel_01.enableLights(true);
                notificationChannel_01.setLockscreenVisibility(0);

                // - create notification manager
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(notificationChannel_01);
            }

        Notification channel1 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(musicSubject)
                .setLargeIcon(largeImage)
                .addAction(R.drawable._6_music_before, "like4", pbeforeintent)
//                    .addAction(R.drawable.music_play, "like1", pplayintent)
                .addAction(R.drawable._6_music_pause, "like2", ppauseintent)
                .addAction(R.drawable._6_music_after, "like5", pafterintent)
                .addAction(R.drawable._6_music_close, "like3", pcloseintent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView())
                .setContentIntent(ptouchintent)
                .setOngoing(true)
                .build();

        notificationManager.notify(1, channel1);
        startForeground(1, channel1);
    }
}
