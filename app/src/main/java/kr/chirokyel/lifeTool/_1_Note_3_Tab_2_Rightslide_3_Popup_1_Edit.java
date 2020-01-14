package kr.chirokyel.lifeTool;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class _1_Note_3_Tab_2_Rightslide_3_Popup_1_Edit extends Activity {

    Uri uri;

    ImageView tab_rightslide_popup_imageview;
    Button tab_rightslide_popup_button_confirm;
    Button tab_rightslide_popup_delete;
    Button tab_rightslide_popup_cancel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._1_note_3_tab_2_rightslide_popup_edit);

        // 인텐트 : 이미지 포지션 받기
        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);

        // 인텐트 : 갤러리에서 이미지 가져오기(1/2)
        uri = Uri.parse(_1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.get(position).getPageImage());
        tab_rightslide_popup_imageview = findViewById(R.id.tab_imageslide_popup_imageview);
        tab_rightslide_popup_imageview.setImageURI(uri);
        tab_rightslide_popup_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });

        // 버튼 : 수정 완료
        tab_rightslide_popup_button_confirm = findViewById(R.id.tab_imageslide_popup_button_confirm);
        tab_rightslide_popup_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.get(position).setPageImage(uri.toString());
                _1_Note_3_Tab_1_Main.page_rightslide_adapter.notifyDataSetChanged();
                finish();
            }
        });

        // 버튼 : 삭제
        tab_rightslide_popup_delete = findViewById(R.id.tab_imageslide_popup_button_delete);
        tab_rightslide_popup_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.remove(position);
                _1_Note_3_Tab_1_Main.page_rightslide_adapter.notifyDataSetChanged();
                finish();
            }
        });

        // 버튼 : 수정 취소
        tab_rightslide_popup_cancel = findViewById(R.id.tab_imageslide_popup_button_cancel);
        tab_rightslide_popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 인텐트 : 갤러리에서 이미지 가져오기(2/2)
        if (requestCode == 0) {
            try{
                // 이미지 uri 받기
                uri = data.getData();
                tab_rightslide_popup_imageview.setImageURI(uri);

                // uri 권한 받기
                getContentResolver().takePersistableUriPermission (uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }catch(Exception e) {
            }
        }
    }
}
