package kr.chirokyel.lifeTool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class _1_Note_2_Page_2_Leftslide_2_Adapter extends RecyclerView.Adapter<_1_Note_2_Page_2_Leftslide_2_Adapter.ViewHolder> implements Filterable {


    Context context;

    // arrayList
    static List<_1_Note_2_Page_2_Leftslide_1_Form> pageSubjectList;
    static List<_1_Note_2_Page_2_Leftslide_1_Form> pageSubjectListAll;

    static int visibility = View.INVISIBLE;



    // constructor
    public _1_Note_2_Page_2_Leftslide_2_Adapter(List<_1_Note_2_Page_2_Leftslide_1_Form> pageSubjectList, Context context) {
        this.pageSubjectList = pageSubjectList;
        pageSubjectListAll = new ArrayList<>();
        pageSubjectListAll.addAll(pageSubjectList);
        this.context = context;
    }



    // item size
    @Override
    public int getItemCount() {
        return pageSubjectList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    // viewHolder : class
    class ViewHolder extends RecyclerView.ViewHolder {

        // viewHolder component
        TextView page_leftslide_form_textview;
        CheckBox page_leftslide_form_checkbox;

        // constructor (1/3)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // - connect component
            page_leftslide_form_textview = itemView.findViewById(R.id.page_pageslide_form_textview);
            page_leftslide_form_checkbox = itemView.findViewById(R.id.page_pageslide_form_checkbox);
        }
    }


    // create viewHolder : method (2/3)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // copy view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout._1_note_2_page_2_leftslide_form, parent, false);

        // mount view
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    // decorate viewHolder : method (3/3)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // connect data
        holder.page_leftslide_form_textview.setText(pageSubjectList.get(position).getPageSubject());


        // connect event
        // - open note : click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), _1_Note_3_Tab_1_Main.class);
                i.putExtra("noteKey", pageSubjectList.get(holder.getAdapterPosition()).noteKey);
                i.putExtra("pageKey", pageSubjectList.get(holder.getAdapterPosition()).pageKey);
                i.putExtra("pageSubject", pageSubjectList.get(holder.getAdapterPosition()).pageSubject);
                context.startActivity(i);
            }
        });

        // - edit subject : longClick
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("내용을 수정하세요");
                final EditText contents1 = new EditText(context);
                contents1.setText(pageSubjectList.get(position).getPageSubject());
                alert.setView(contents1);

                // - confirm : button
                alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String contents2 = contents1.getText().toString();
                        _1_Note_2_Page_2_Leftslide_2_Adapter.pageSubjectList.get(position).setPageSubject(contents2);
                        notifyDataSetChanged();
                    }
                });

                // - cancel : button
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                notifyDataSetChanged(); // 팝업 바깥 눌러도 바로 원상태 만드려면.
                alert.show();
                return true;
            }
        });


        // - for conserve checked state in recycling holder (1/2)
        final _1_Note_2_Page_2_Leftslide_1_Form object = pageSubjectList.get(position);
        holder.page_leftslide_form_checkbox.setOnCheckedChangeListener(null);
        holder.page_leftslide_form_checkbox.setChecked(object.stateChecked);


        // - control visible using variable 'visibility(int)'
        if (pageSubjectList.get(position).stateVisible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        holder.page_leftslide_form_checkbox.setVisibility(visibility);


        // - checkbox checkable : checkChangeListner
        holder.page_leftslide_form_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // - for conserve state in recycling holder (2/2)
                object.setStateChecked(isChecked);

                if (buttonView.isChecked()) {
                    pageSubjectList.get(position).setStateChecked(true);
                } else {
                    pageSubjectList.get(position).setStateChecked(false);
                }
            }
        });
    }








    // search : interface
    // - mount filter
    @Override
    public Filter getFilter() {
        return pageFilter;
    }

    // - operate filter
    Filter pageFilter = new Filter() {

        // - background filtering
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            // filtered ArrayList
            List<_1_Note_2_Page_2_Leftslide_1_Form> pageSubjectlistFiltered = new ArrayList<>();

            // nothing
            if (constraint == null || constraint.length() == 0) {
                pageSubjectlistFiltered.addAll(pageSubjectListAll);

            // something
            } else {
                for (_1_Note_2_Page_2_Leftslide_1_Form page : pageSubjectListAll) {
                    if (page.getPageSubject().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        pageSubjectlistFiltered.add(page);
                    }
                }
            }

            // filterResults
            FilterResults filterResults = new FilterResults();
            filterResults.values = pageSubjectlistFiltered;
            return filterResults;
        }

        // foreground filtering
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // apply result
            pageSubjectList.clear();
            pageSubjectList.addAll((Collection<? extends _1_Note_2_Page_2_Leftslide_1_Form>) results.values);
            notifyDataSetChanged();
        }
    };
}
