package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.logging.Handler;

public class _9_Book_2_Adapter extends RecyclerView.Adapter<_9_Book_2_Adapter.ViewHolder> {

    Context context;
    static List<_9_Book_1_Form> bookList;
    Handler handler;

    // 어댑터 생성자
    public _9_Book_2_Adapter(List<_9_Book_1_Form> bookList) {
        this.bookList = bookList;
    }

    // 아이템 갯수
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // 클래스 : 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        LinearLayout book_form_linearlayout;
        ImageView book_form_imageview;
        TextView book_form_title;
        TextView book_form_author;
        TextView book_form_publisher;
        TextView book_form_description;


        // 뷰홀더 생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연결
            book_form_linearlayout = itemView.findViewById(R.id.book_form_linearlayout);
            book_form_imageview = itemView.findViewById(R.id.book_form_imageview);
            book_form_title = itemView.findViewById(R.id.book_form_title);
            book_form_author = itemView.findViewById(R.id.book_form_author);
            book_form_publisher = itemView.findViewById(R.id.book_form_publisher);
            book_form_description = itemView.findViewById(R.id.book_form_description);
        }
    }

    // 뷰홀더 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._9_book_1_form, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    // 뷰홀더 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.book_form_title.setText("제목 : " + StringReplace(bookList.get(position).getTitle()));
        holder.book_form_author.setText("저자 : " + StringReplace(bookList.get(position).getAuthor()));
        holder.book_form_publisher.setText("출판 : " + StringReplace(bookList.get(position).getPublisher()));
        holder.book_form_description.setText("내용 : " + StringReplace(bookList.get(position).getDescription()));

        Glide.with(context).load(bookList.get(position).getImage()).into(holder.book_form_imageview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookList.get(position).getLink()));
                context.startActivity(intent);
            }
        });
    }

    public static String StringReplace(String str){
        str.replace("<", "");
        str.replace("b", "");
        str.replace("/", "");
        str.replace(">", "");
        str.replace("&", "");
        return str;
    }
}
