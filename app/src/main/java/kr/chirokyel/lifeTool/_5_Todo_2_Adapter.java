package kr.chirokyel.lifeTool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class _5_Todo_2_Adapter extends RecyclerView.Adapter<_5_Todo_2_Adapter.CustomViewHolder> {

    static ArrayList<_5_Todo_1_Form> todolist;
    static int visibility;


    // 어댑터 생성자
    public _5_Todo_2_Adapter(ArrayList<_5_Todo_1_Form> todolist) {
        this.todolist = todolist;
    }

    // 아이템 갯수
    @Override
    public int getItemCount() {
        return todolist.size();
    }

    // 메서드 : 아이템 추가
    public void addtodo(String todo, int todoKey) {
        _5_Todo_1_Form addlist = new _5_Todo_1_Form();
        addlist.setTodo(todo);
        addlist.setTodoKey(todoKey);
        todolist.add(addlist);
    }


    // 클래스 : 뷰홀더
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        ImageView todo_form_imageview_alram;
        TextView todo_form_textview_alram;
        protected TextView todo_form_textview_todo;


        // 뷰홀더 생성자
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연걸
            this.todo_form_textview_todo = itemView.findViewById(R.id.todo_form_textview_todo);
            this.todo_form_imageview_alram = itemView.findViewById(R.id.todo_form_imageview_alarm);
            this.todo_form_textview_alram = itemView.findViewById(R.id.todo_form_textview_alarm);
        }
    }


    // 뷰홀더 생성
    @NonNull
    @Override
    public _5_Todo_2_Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout._5_todo_1_form, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }


    // 뷰홀더 연결
    @Override
    public void onBindViewHolder(@NonNull _5_Todo_2_Adapter.CustomViewHolder holder, final int position) {

        // 할일, 알람 설정
        holder.todo_form_textview_todo.setText(todolist.get(position).getTodo());
        holder.todo_form_textview_alram.setText(todolist.get(position).getSettingTime());


        // 알람 정보 나타나게, 사라지게
        if (todolist.get(position).stateVisible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        holder.todo_form_imageview_alram.setVisibility(visibility);
    }
}
