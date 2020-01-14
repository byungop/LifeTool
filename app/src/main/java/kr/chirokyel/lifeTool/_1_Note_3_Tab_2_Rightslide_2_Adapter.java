package kr.chirokyel.lifeTool;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class _1_Note_3_Tab_2_Rightslide_2_Adapter extends BaseAdapter {

    // arrayList
    static ArrayList<_1_Note_3_Tab_2_Rightslide_1_Form> tabImageList = new ArrayList<>();

    // item size
    @Override
    public int getCount() {
        return tabImageList.size();
    }

    // item
    @Override
    public Object getItem(int position) {
        return tabImageList.get(position);
    }

    // item id
    @Override
    public long getItemId(int position) {
        return position;
    }

    // add item
    public void addpicture(String pagePicture) {
        _1_Note_3_Tab_2_Rightslide_1_Form addpicture = new _1_Note_3_Tab_2_Rightslide_1_Form();
        addpicture.setPageImage(pagePicture);
        tabImageList.add(addpicture);
    }

    // create view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            // copy view
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // mount view
            convertView = layoutInflater.inflate(R.layout._1_note_3_tab_2_rightslide_form, parent, false);
        }

        // connect component
        ImageView tab_rightslide_form_imageview = convertView.findViewById(R.id.tab_imageslide_form_imageview);

        // connect data
        _1_Note_3_Tab_2_Rightslide_1_Form pageImage = tabImageList.get(position);
        tab_rightslide_form_imageview.setImageURI(Uri.parse(pageImage.getPageImage()));

        return convertView;
    }
}
