package kr.chirokyel.lifeTool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class _3_Step_2_Adapter extends RecyclerView.Adapter<_3_Step_2_Adapter.ViewHolder> {

    static Context context;
    static List<_3_Step_1_Form> stepList;

    // 어댑터 생성자
    public _3_Step_2_Adapter(List<_3_Step_1_Form> stepList, Context context) {
        this.stepList = stepList;
        this.context = context;
    }

    // 아이템 갯수
    @Override
    public int getItemCount() {
        return stepList.size();
    }

    // 메서드 : 아이템 추가
    public void addStep(String date, String step, String distance) {
        _3_Step_1_Form addStep = new _3_Step_1_Form();
        addStep.setDate(date);
        addStep.setStep(step);
        addStep.setDistance(distance);
        stepList.add(addStep);
    }

    // 클래스 : 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        TextView step_form_textview_date;
        TextView step_form_textview_step;
        TextView step_form_textview_distance;

        // 뷰홀더 생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연결
            step_form_textview_date = itemView.findViewById(R.id.step_form_textview_date);
            step_form_textview_step = itemView.findViewById(R.id.step_form_tetxtview_step);
            step_form_textview_distance = itemView.findViewById(R.id.step_form_textview_distance);

            // 아이템 : 아이템 제거, 롱클릭
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    stepList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Toast.makeText(context, "삭제!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    // 뷰홀더 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._3_step_1_form, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    // 뷰홀더 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.step_form_textview_date.setText(stepList.get(position).getDate());
        holder.step_form_textview_step.setText(stepList.get(position).getStep());
        holder.step_form_textview_distance.setText(stepList.get(position).getDistance());
    }
}
