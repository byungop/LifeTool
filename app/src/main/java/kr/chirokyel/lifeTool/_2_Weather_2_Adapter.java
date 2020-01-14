package kr.chirokyel.lifeTool;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class _2_Weather_2_Adapter extends RecyclerView.Adapter<_2_Weather_2_Adapter.ViewHolder> {

    Context context;
    List<_2_Weather_1_Form> weatherList;

    // 어댑터 생성자
    public _2_Weather_2_Adapter(List<_2_Weather_1_Form> weatherList) {
        this.weatherList = weatherList;
    }


    // 아이템 갯수
    @Override
    public int getItemCount() {
        return weatherList.size();
    }



    // 클래스 : 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {

        // 뷰홀더 구성요소 선언
        TextView weather_form_textview1;
        ImageView weather_form_imageview;
        TextView weather_form_textview2;


        // 뷰홀더 생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰홀더 구성요소 연결
            weather_form_textview1 = itemView.findViewById(R.id.weather_form_textview1);
            weather_form_imageview = itemView.findViewById(R.id.weather_form_imageview);
            weather_form_textview2 = itemView.findViewById(R.id.weather_form_textview2);
        }
    }

    // 뷰홀더 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰 복제
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._2_weather_1_form, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    // 뷰홀더 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // 날씨정보 설정
        holder.weather_form_textview1.setText(weatherList.get(position).getDate() + " " + weatherList.get(position).getTime() + "시");
        holder.weather_form_imageview.setImageURI(Uri.parse(weatherList.get(position).getImage()));
        holder.weather_form_textview2.setText(weatherList.get(position).getCurTemp() + "°C" + "\n" +
                 "( 강수확률 : " + weatherList.get(position).getWaterP() + "% )");
    }

}
