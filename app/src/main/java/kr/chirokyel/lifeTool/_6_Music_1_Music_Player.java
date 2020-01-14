package kr.chirokyel.lifeTool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class _6_Music_1_Music_Player extends AppCompatActivity {


    Uri uri;

    int duration;
    int currentPosition;
    int pausePosition;


    int seekbarCode = 0;

    // 기본 구성요소
    TextView music_player_textview;
    String musicSubject;
    Button music_player_button_choice;
    Button music_player_button_play; int buttonCode = 0;
    SeekBar music_player_seekbar; Handler handler; Runnable runnable;

    // 서비스 : 바인드
    _6_Music_2_Service music_service;
    boolean isbound = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._6_music_1_player);

        // 메서드 : 데이터 불러오기
        loadData();

        // 서비스 : 바인드(2/2)
        Intent intent = new Intent(_6_Music_1_Music_Player.this, _6_Music_2_Service.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        // 텍스트뷰 : 제목 달기
        music_player_textview = findViewById(R.id.music_player_textview);
        music_player_textview.setSelected(true);
        music_player_textview.setText(musicSubject);

        // 버튼 : 음악선택(1/2)
        music_player_button_choice = findViewById(R.id.music_player_button_choice);
        music_player_button_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                i.setType("audio/*");
                startActivityForResult(i, 0);
            }
        });

        // 버튼 : 음악 재생
        music_player_button_play = findViewById(R.id.music_player_button_play);
        music_player_button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 음악 재생하는 경우
                if (buttonCode == 0) {

                        // uri 가지고 있지 않은 경우
                    if (uri == null) {
                        Toast.makeText(getApplicationContext(), "음악을 선택해주세요", Toast.LENGTH_SHORT).show();

                        // uri 가지고 있는 경우
                    } else if (uri != null) {

                        // 서비스 바인드 연결
                        if (!isbound) {
                            Intent intent = new Intent(_6_Music_1_Music_Player.this, _6_Music_2_Service.class);
                            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                        }

                        // 인텐트 : 서비스 스타트
                        Intent intent = new Intent(_6_Music_1_Music_Player.this, _6_Music_2_Service.class);
                        startService(intent);

                        // 메서드 : 씩바 작동
                        if (seekbarCode == 0) {
                            runSeekbar();
                            music_player_seekbar.setMax(music_service.getDuration());
                        }

                        // 재생 상태로 전환
                        buttonCode = 1;
                        music_player_button_play.setText("일시정지");
                    }


                // 음악 정지하는 경우
                } else if (buttonCode == 1) {

                    // 바인드 : 미디어 플레이어 정지
                    music_service.setMediaPlayerPause();

                    // 정지 상태로 전환
                    buttonCode = 0;
                    music_player_button_play.setText("재생");


                // 음악 바꾸는 경우
                } else if (buttonCode == 2) {

                    // 바인드 : 음악 uri 변경
                    music_service.setMusicUri(uri, musicSubject);
                    music_service.changeMusic();
                    music_player_seekbar.setMax(music_service.getDuration());

                    // 재생 상태로 전환
                    buttonCode = 1;
                    music_player_button_play.setText("일시정지");
                }
            }
        });

        // 메서드 : 씩바 작동
        runSeekbar();
    }


    @Override
    protected void onStop() {
        super.onStop();

        // 데이터 저장하기
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 바인드 해제하기
        if (isbound) {
            unbindService(serviceConnection);
            isbound = false;
        }
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    // 서비스 : 바인드(1/2)
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override

        // 바인드 연결
        public void onServiceConnected(ComponentName name, IBinder service) {
            _6_Music_2_Service.MusicBinder musicBinder = (_6_Music_2_Service.MusicBinder) service;
            music_service = (_6_Music_2_Service) musicBinder.getService();
            isbound = true;
        }

        // 바인드 해제
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isbound = false;
        }
    };


    // 인텐트
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // 인텐트 : 음악선택(2/2)
        if (requestCode == 0) {
            try {

                uri = data.getData();
                musicSubject = getFileName(uri);
                music_player_textview.setText(musicSubject);
                music_service.setMusicUri(uri, musicSubject);

                getContentResolver().takePersistableUriPermission(uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            } catch (Exception e) {
            }

            // 다른 음악 재생 상태로 전환
            music_player_button_play.setText("다른 음악 재생");
            buttonCode = 2;
        }
    }


    // 메서드 : Uri로 음악 제목 가져오기
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    // 메서드 : 씩바 작동
    public void runSeekbar() {

        music_player_seekbar = findViewById(R.id.music_player_seekbar);
        handler = new Handler();

        // 서비스로부터 미디어플레이어 상태 받아오기
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    handler.postDelayed(this, 500);
                    music_player_seekbar.setMax(music_service.getDuration());
                    music_player_seekbar.setProgress(music_service.getCurrentPosition());
                    if (buttonCode == 2) {
                    } else if (music_service.getMediaPlayerState()) {
                        music_player_button_play.setText("일시정지");
                        buttonCode = 1;
                    } else {
                        music_player_button_play.setText("재생");
                        buttonCode = 0;
                    }

                } catch (NullPointerException e) {
                }
            }
        };

        // 식바 작동상태로 전환
        runnable.run();
        seekbarCode = 1;


        // 서비스로 씩바 상태 전달하기
        music_player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    music_service.setSeekbarPosition(i);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    // 먼지 몰겠음.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    // 메서드 : 데이터 저장하기
    private void saveData() {

        // 쉐어드 생성하기
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 변수 저장하기
        try {
            editor.putString("musicSubject", musicSubject); // subject
            editor.putString("musicURI", uri.toString()); // uri
            editor.putInt("duration", duration); // duration
            editor.putInt("currentPosition", currentPosition); // currentPosition
            editor.putInt("pausePosition", pausePosition);  // pausePosition
            editor.apply();
        } catch (NullPointerException e) {

        }
    }


    // 메서드 : 데이터 불러오기
    private void loadData() {

        // 쉐어드 생성하기
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // 변수 불러오기
        try {
            this.musicSubject = sharedPreferences.getString("musicSubject", null);
            this.uri = Uri.parse(sharedPreferences.getString("musicURI", null));
            this.duration = sharedPreferences.getInt("duration", 0);
            this.currentPosition = sharedPreferences.getInt("currentPosition", 0);
            this.pausePosition = sharedPreferences.getInt("pausePosition", 0);
        } catch (NullPointerException e) {
        }





        if (musicSubject == null) {
            this.musicSubject = "음악을 선택해주세요.     음악을 선택해주세요.";
        }

    }

}
