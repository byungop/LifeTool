package kr.chirokyel.lifeTool;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class _1_Note_1_Note_4_popup extends Activity {

    Uri uri;

    ImageView note_popup_imageview;
    TextView note_popup_textview;
    Button note_popup_button_confirm;
    Button note_popup_button_delete;
    Button note_button_cancel;

    _1_Note_1_Note_2_Adapter note_1_note_2_adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._1_note_1_note_3_popup);


        // 인텐트 : 아이템 포지션 받기
        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);
        System.out.println("popup" + position);


        // 이미지뷰 : 인텐트, 갤러리에서 이미지 가져오기 (1/2)
        note_popup_imageview = findViewById(R.id.note_popup_imageview);
        uri = Uri.parse(_1_Note_1_Note_2_Adapter.noteList.get(position).getNoteCover());
        note_popup_imageview.setImageURI(uri);
        note_popup_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, 0);
            }
        });

        // 텍스트뷰 : 제목 입력 다이얼로그
        note_popup_textview = findViewById(R.id.note_popup_textview);
        note_popup_textview.setText(_1_Note_1_Note_2_Adapter.noteList.get(position).getNoteSubject());
        note_popup_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(_1_Note_1_Note_4_popup.this);
                alert.setMessage("내용을 입력해주세요");
                final EditText contents1 = new EditText(_1_Note_1_Note_4_popup.this);
                contents1.setText(_1_Note_1_Note_2_Adapter.noteList.get(position).getNoteSubject());
                alert.setView(contents1);

                // 버튼 : 확인
                alert.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String contents2 = contents1.getText().toString();
                        note_popup_textview.setText(contents2);
                    }
                });

                // 버튼 : 취소
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });


        // 버튼 : 수정 완료
        note_popup_button_confirm = findViewById(R.id.note_popup_button_confirm);
        note_popup_button_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                _1_Note_1_Note_2_Adapter.noteList.get(position).setNoteCover(uri.toString());
                _1_Note_1_Note_2_Adapter.noteList.get(position).setNoteSubject(note_popup_textview.getText().toString());
                System.out.println(position + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                _1_Note_1_Note_3_Main_Fragment.note_2_adapter.notifyDataSetChanged();
                System.out.println(position + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                finish();
            }
        });

        // 버튼 : 삭제
        note_popup_button_delete = findViewById(R.id.note_popup_button_delete);
        note_popup_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("아이템 삭제@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                _1_Note_1_Note_2_Adapter.noteList.remove(position);
                _1_Note_1_Note_3_Main_Fragment.note_2_adapter.notifyDataSetChanged();
                finish();
            }
        });

        // 버튼 : 수정 취소
        note_button_cancel = findViewById(R.id.note_popup_button_cancel);
        note_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 이미지뷰 : 인텐트, 갤러리에서 이미지 가져오기 (1/2)
        if (requestCode == 0) {
            try{

                // 이미지 uri 받기
                uri = data.getData();
                note_popup_imageview.setImageURI(uri);

                // 이미지 uri 권한 받기
                getContentResolver().takePersistableUriPermission (uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }catch(Exception e) {
            }
        }
    }
}
