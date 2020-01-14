package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class _1_Note_1_Note_2_Adapter extends RecyclerView.Adapter<_1_Note_1_Note_2_Adapter.ViewHolder> {


    Context context;
    static List<_1_Note_1_Note_1_Form> noteList;


    // 어댑터 생성자
    public _1_Note_1_Note_2_Adapter(List<_1_Note_1_Note_1_Form> noteSubjectList, Context context) {
        this.noteList = noteSubjectList;
        this.context = context;
    }

    // 아이템 갯수
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    // 메서드 : 아이템 추가
    public void addNote(String noteCover, String noteSubject, int noteKey) {
        _1_Note_1_Note_1_Form addNote = new _1_Note_1_Note_1_Form();
        addNote.setNoteCover(noteCover);
        addNote.setNoteSubject(noteSubject);
        addNote.setNoteKey(noteKey);
        noteList.add(addNote);
    }



    // 클래스 : 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        Button note_form_button;
        ImageView note_form_imageview;
        TextView note_form_textview;

        // 뷰홀더 생성자
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연걸
            note_form_button = itemView.findViewById(R.id.note_form_button);
            note_form_imageview = itemView.findViewById(R.id.note_form_imageview);
            note_form_textview = itemView.findViewById(R.id.note_form_textview);
        }
    }

    // 뷰홀더 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._1_note_1_note_1_form, parent, false);

        // 뷰 탑제
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    // 뷰홀더 연결
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // 데이터 연결
        holder.note_form_imageview.setImageURI(Uri.parse(noteList.get(position).getNoteCover()));
        holder.note_form_textview.setText(noteList.get(position).getNoteSubject());


        // 버튼 : 인텐트, 수정 액티비티로 화면전환, 온클릭
        holder.note_form_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, _1_Note_1_Note_4_popup.class);
                i.putExtra("position", holder.getAdapterPosition());
                System.out.println("holder" + holder.getAdapterPosition());
                context.startActivity(i);
            }
        });

        // 아이템 : 인텐트, 페이지 액티비티로 화면전환, 온클릭
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, _1_Note_2_Page_1_Main.class);
                i.putExtra("noteKey", noteList.get(holder.getAdapterPosition()).noteKey);
                i.putExtra("noteSubject", noteList.get(holder.getAdapterPosition()).noteSubject);
                context.startActivity(i);
            }
        });
    }
}
