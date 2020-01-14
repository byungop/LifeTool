package kr.chirokyel.lifeTool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class _1_Note_3_Tab_1_Main_Adapter extends RecyclerView.Adapter<_1_Note_3_Tab_1_Main_Adapter.ViewHolder> {

    // arrayList
    static List<String> pageTextList;

    // constructor
    public _1_Note_3_Tab_1_Main_Adapter(List<String> pageContentsList) {
        this.pageTextList = pageContentsList;
    }

    // item size
    @Override
    public int getItemCount() {
        return pageTextList.size();
    }


    // viewHolder : class
    public class ViewHolder extends RecyclerView.ViewHolder {

        // viewHolder component
        TextView tab_main_form_textview;

        // assemble viewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // connect component
            tab_main_form_textview = itemView.findViewById(R.id.tab_main_form_textview);
        }
    }

    // create viewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // copy view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._1_note_3_tab_1_main_form, parent, false);

        // mount view
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // decorate viewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // connect data
        holder.tab_main_form_textview.setText(pageTextList.get(position));
    }
}
