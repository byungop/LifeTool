package kr.chirokyel.lifeTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class _1_Note_3_Tab_2_Rightslide_3_Popup_2_order extends Activity {


    public int position_move;
    Button tab_rightslide_popup_button_up;
    Button tab_rightslide_popup_button_down;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._1_note_3_tab_2_rightslide_popup_order);

        // 인텐트 : 이미지 포지션 받기
        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);


        // 버튼 : 이미지 위로 이동
        position_move = position;
        tab_rightslide_popup_button_up = findViewById(R.id.tab_imageslide_popup_button_up);
        tab_rightslide_popup_button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position_move == 0) {
                    Toast.makeText(getApplicationContext(), "처음.", Toast.LENGTH_SHORT).show();
                } else {
                    _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.add(position_move -1,
                            _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.get(position_move));
                    _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.remove(position_move +1);
                    position_move--;
                    _1_Note_3_Tab_1_Main.page_rightslide_adapter.notifyDataSetChanged();
                }
            }
        });


        // 버튼 : 이미지 아래로 이동
        tab_rightslide_popup_button_down = findViewById(R.id.tab_imageslide_popup_button_down);
        tab_rightslide_popup_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position_move == _1_Note_3_Tab_1_Main.page_rightslide_adapter.getCount()-1) {
                    Toast.makeText(getApplicationContext(), "끝.", Toast.LENGTH_SHORT).show();
                } else {
                    _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.add(position_move +2,
                            _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.get(position_move));
                    _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList.remove(position_move);
                    position_move++;
                    _1_Note_3_Tab_1_Main.page_rightslide_adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
