package kr.chirokyel.lifeTool;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class _4_Plan_2_Adatper extends RecyclerView.Adapter<_4_Plan_2_Adatper.CustomViewHolder> {

    private ArrayList<_4_Plan_1_Form> planlist;

    // 어댑터 생성자
    public _4_Plan_2_Adatper(ArrayList<_4_Plan_1_Form> planlist) {
        this.planlist = planlist;
    }

    // 아이템 갯수
    @Override
    public int getItemCount() {
        return planlist.size();
    }

    // 메서드 : 아이템 추가
    public void addplan(String start, String end, String plan, String color) {
        _4_Plan_1_Form addplan = new _4_Plan_1_Form(start, end, plan, color);
        addplan.setStart(start);
        addplan.setEnd(end);
        addplan.setPlan(plan);
        addplan.setColor(color);
        planlist.add(addplan);
    }

    // 클래스 : 뷰홀더
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        protected LinearLayout plan_form_layout;
        protected TextView plan_form_start;
        protected TextView plan_form_end;
        protected TextView plan_form_what;

        protected Button plan_popub_button_yellow;
        protected Button plan_popub_button_green;
        protected Button plan_popub_button_blue;
        protected Button plan_popub_button_purple;


        // 뷰홀더 생성자
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연결
            this.plan_form_start = (TextView) itemView.findViewById(R.id.plan_form_start);
            this.plan_form_end = (TextView) itemView.findViewById(R.id.plan_form_end);
            this.plan_form_what = (TextView) itemView.findViewById(R.id.plan_form_what);
            this.plan_form_layout = (LinearLayout) itemView.findViewById(R.id.plan_form_layout);

            this.plan_popub_button_yellow = itemView.findViewById(R.id.plan_popup_button_yellow);
            this.plan_popub_button_green = itemView.findViewById(R.id.plan_popup_button_green);
            this.plan_popub_button_blue = itemView.findViewById(R.id.plan_popup_button_blue);
            this.plan_popub_button_purple = itemView.findViewById(R.id.plan_popup_button_purple);
        }
    }


    // 뷰홀더 생성
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout._4_plan_1_form, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }


    // 뷰홀더 연결
    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        // 시간, 내용 설정
        holder.plan_form_start.setText(planlist.get(position).getStart());
        holder.plan_form_end.setText(planlist.get(position).getEnd());
        holder.plan_form_what.setText(planlist.get(position).getPlan());

        try {
            // 색깔 설정
            if (_4_Plan_3_Main_Fragment.planlist.get(position).getColor().equals("1")) {
                holder.plan_form_layout.setBackgroundResource(R.drawable._4_plan_yellow);
            } else if (_4_Plan_3_Main_Fragment.planlist.get(position).getColor().equals("2")) {
                holder.plan_form_layout.setBackgroundResource(R.drawable._4_plan_green);
            } else if (_4_Plan_3_Main_Fragment.planlist.get(position).getColor().equals("3")) {
                holder.plan_form_layout.setBackgroundResource(R.drawable._4_plan_blue);
            } else if (_4_Plan_3_Main_Fragment.planlist.get(position).getColor().equals("4")) {
                holder.plan_form_layout.setBackgroundResource(R.drawable._4_plan_purple);
            } else if (_4_Plan_3_Main_Fragment.planlist.get(position).getColor().equals("5")) {
                holder.plan_form_layout.setBackgroundColor(Color.parseColor("#BAFFFEFE"));
            }
        } catch (NullPointerException e) {

        }

    }
}
