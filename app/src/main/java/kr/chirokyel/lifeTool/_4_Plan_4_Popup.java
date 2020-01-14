package kr.chirokyel.lifeTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class _4_Plan_4_Popup extends Activity {

    EditText plan_popup_edittext_start;
    EditText plan_popup_edittext_end;
    EditText plan_popup_edittext_plan;

    Button plan_popup_button_edit;
    Button plan_popup_button_cancel;

    Button plan_popup_button_yellow;
    Button plan_popup_button_green;
    Button plan_popup_button_blue;
    Button plan_popup_button_purple;

    String color;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._4_plan_3_popup);

        // 인텐트 : 아이템 상태 받기
        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);

        plan_popup_edittext_start = findViewById(R.id.plan_popup_edittext_start);
        plan_popup_edittext_end = findViewById(R.id.plan_popup_edittext_end);
        plan_popup_edittext_plan = findViewById(R.id.plan_popup_edittext_plan);

        plan_popup_edittext_start.setText(_4_Plan_3_Main_Fragment.planlist.get(position).getStart());
        plan_popup_edittext_end.setText(_4_Plan_3_Main_Fragment.planlist.get(position).getEnd());
        plan_popup_edittext_plan.setText(_4_Plan_3_Main_Fragment.planlist.get(position).getPlan());

        // 버튼 : 수정
        plan_popup_button_edit = findViewById(R.id.plan_popup_button_edit);
        plan_popup_button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _4_Plan_3_Main_Fragment.planlist.get(position).setStart(plan_popup_edittext_start.getText().toString());
                _4_Plan_3_Main_Fragment.planlist.get(position).setEnd(plan_popup_edittext_end.getText().toString());
                _4_Plan_3_Main_Fragment.planlist.get(position).setPlan(plan_popup_edittext_plan.getText().toString());
                _4_Plan_3_Main_Fragment.planlist.get(position).setColor(color);
                _4_Plan_3_Main_Fragment.plan_adatper.notifyDataSetChanged();
                System.out.println(_4_Plan_3_Main_Fragment.planlist.get(position).getColor()  + "팝업");
                finish();
            }
        });

        // 버튼 : 취소
        plan_popup_button_cancel = findViewById(R.id.plan_popup_button_cancel);
        plan_popup_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _4_Plan_3_Main_Fragment.plan_adatper.notifyDataSetChanged();
                finish();
            }
        });

        // 버튼 : 노란색
        plan_popup_button_yellow = findViewById(R.id.plan_popup_button_yellow);
        plan_popup_button_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = "1";
                _4_Plan_3_Main_Fragment.planCode = "컬러";
            }
        });

        // 버튼 : 초록색
        plan_popup_button_green = findViewById(R.id.plan_popup_button_green);
        plan_popup_button_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = "2";
                _4_Plan_3_Main_Fragment.planCode = "컬러";
            }
        });

        // 버튼 : 파랑색
        plan_popup_button_blue = findViewById(R.id.plan_popup_button_blue);
        plan_popup_button_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = "3";
                _4_Plan_3_Main_Fragment.planCode = "컬러";
            }
        });

        // 버튼 : 보라색
        plan_popup_button_purple = findViewById(R.id.plan_popup_button_purple);
        plan_popup_button_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = "4";
                _4_Plan_3_Main_Fragment.planCode = "컬러";
            }
        });

    }
}
