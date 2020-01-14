package kr.chirokyel.lifeTool;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class _1_Note_3_Tab_1_Main extends AppCompatActivity {

    // keyCode
    private int noteKey;
    private int pageKey;

    // intent
    private String pageSubject;


    // main component

    DrawerLayout tab_main_drawerlayout;
    List<String> tabTextList;
    _1_Note_3_Tab_1_Main_Adapter tab_main_adapter;
    RecyclerView tab_main_recyclerView;
    Button tab_main_button_text;
    Button tab_main_button_drawer;

    // rightslide

    ArrayList<_1_Note_3_Tab_2_Rightslide_1_Form> tabImageList;
    static _1_Note_3_Tab_2_Rightslide_2_Adapter page_rightslide_adapter;
    ListView tab_rightslide_listview;

    Uri uri;

    TextView tab_main_textview;
    Button tab_rightslide_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._1_note_3_tab_1_main);


        // get page : intent
        Intent i = getIntent();
        noteKey = i.getIntExtra("noteKey", 0);
        pageKey = i.getIntExtra("pageKey", 0);
        pageSubject = i.getStringExtra("pageSubject");

        // load data : sharedPreferences
        loadData();
        _1_Note_3_Tab_2_Rightslide_2_Adapter.tabImageList = tabImageList; // data change apply for listview


        // connect recyclerview
        tab_main_recyclerView = findViewById(R.id.tab_main_recyclerView);
        tab_main_adapter = new _1_Note_3_Tab_1_Main_Adapter(tabTextList);
        tab_main_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tab_main_recyclerView.setAdapter(tab_main_adapter);

        // add text : button
        tab_main_button_text = findViewById(R.id.tab_main_button_add);
        tab_main_button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabTextList.add("새컨텐츠");
                tab_main_adapter.notifyDataSetChanged();
            }
        });

        // open close drawer : button
        tab_main_drawerlayout = findViewById(R.id.tab_main_drawerlayout);
        tab_main_button_drawer = findViewById(R.id.tab_main_button_drawer);
        tab_main_button_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    tab_main_drawerlayout.openDrawer(Gravity.RIGHT);
            }
        });


        // drag, drop, swipe : recyclerView (1/2)
        ItemTouchHelper itemTouchHelperMain = new ItemTouchHelper(tab_main_simpleCallback);
        itemTouchHelperMain.attachToRecyclerView(tab_main_recyclerView);


        // (main)
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // (right slide)


        // set pageSubject : textview
        tab_main_textview = findViewById(R.id.tab_main_textview);
        tab_main_textview.setText(pageSubject);

        // connect listview
        tab_rightslide_listview = findViewById(R.id.tab_rightslide_listview);
        page_rightslide_adapter = new _1_Note_3_Tab_2_Rightslide_2_Adapter();
        tab_rightslide_listview.setAdapter(page_rightslide_adapter);

        // get image from gallery : button (1/2)
        tab_rightslide_button = findViewById(R.id.tab_rightslide_button);
        tab_rightslide_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, 0);
            }
        });

        // change item order : onClick event
        tab_rightslide_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), _1_Note_3_Tab_2_Rightslide_3_Popup_2_order.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        // edit item : onLongClick event
        tab_rightslide_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), _1_Note_3_Tab_2_Rightslide_3_Popup_1_Edit.class);
                i.putExtra("position", position);
                startActivity(i);
                return true;
            }
        });
    }



    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // for recovery snackbar
    String wallpaper_deleted = null;
    String wallpaper_edited = null;

    // drag, drop, swipe : recyclerView (2/2)
    ItemTouchHelper.SimpleCallback tab_main_simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        // drag, drop
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(_1_Note_3_Tab_1_Main_Adapter.pageTextList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        // create swipe
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {

                // left (delete)
                case ItemTouchHelper.LEFT:

                    // delete
                    wallpaper_deleted = _1_Note_3_Tab_1_Main_Adapter.pageTextList.get(position);
                    _1_Note_3_Tab_1_Main_Adapter.pageTextList.remove(position);
                    tab_main_adapter.notifyItemRemoved(position);

                    // recovery
                    Snackbar.make(tab_main_recyclerView, wallpaper_deleted, Snackbar.LENGTH_LONG)
                            .setAction("원상태로", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    _1_Note_3_Tab_1_Main_Adapter.pageTextList.add(position, wallpaper_deleted);
                                    tab_main_adapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;

                // right (edit)
                case ItemTouchHelper.RIGHT:

                    // edit
                    wallpaper_edited = _1_Note_3_Tab_1_Main_Adapter.pageTextList.get(position);
                    AlertDialog.Builder alert = new AlertDialog.Builder(_1_Note_3_Tab_1_Main.this);
                    alert.setMessage("내용을 수정하세요");
                    final EditText contents1 = new EditText(_1_Note_3_Tab_1_Main.this);
                    contents1.setText(_1_Note_3_Tab_1_Main_Adapter.pageTextList.get(position));
                    alert.setView(contents1);

                    // confirm : button
                    alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String contents2 = contents1.getText().toString();
                            _1_Note_3_Tab_1_Main_Adapter.pageTextList.set(position, contents2);
                            tab_main_adapter.notifyDataSetChanged();

                            // recovery
                            Snackbar.make(tab_main_recyclerView, wallpaper_edited, Snackbar.LENGTH_LONG)
                                    .setAction("원상태로", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            _1_Note_3_Tab_1_Main_Adapter.pageTextList.set(position, wallpaper_edited);
                                            tab_main_adapter.notifyDataSetChanged();
                                        }
                                    }).show();
                        }
                    });

                    // cancel : button
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    tab_main_adapter.notifyDataSetChanged(); // 팝업 바깥 눌러도 바로 원상태 만드려면.
                    alert.show();
                    break;
            }
        }


        // decorate swipe
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                    // left
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSwipeRed))
                    .addSwipeLeftLabel("DELETE")
                    .setSwipeLeftLabelColor(R.color.colorSwipeText)
                    .setSwipeLeftLabelTextSize(0, 20)
                    .addSwipeLeftActionIcon(R.drawable._0_basic_form_delete)

                    // right
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSwipeBlue))
                    .addSwipeRightLabel("EDIT")
                    .setSwipeRightLabelColor(R.color.colorSwipeText)
                    .setSwipeRightLabelTextSize(0, 20)
                    .addSwipeRightActionIcon(R.drawable._0_basic_form_edit)

                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



    // (main)
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // (right slide)


    // get image from gallery : button (2/2)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get image from gallery : button (2/2)
        if (requestCode == 0) {
            try {

                // get image
                uri = data.getData();
                _1_Note_3_Tab_1_Main.page_rightslide_adapter.addpicture(uri.toString());
                _1_Note_3_Tab_1_Main.page_rightslide_adapter.notifyDataSetChanged();

                // get permission
                getContentResolver().takePersistableUriPermission(uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            } catch (Exception e) {
            }
        }
    }

    // save data
    private void saveData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save arrayList
        Gson gson = new Gson();

        String tabTextList = gson.toJson(this.tabTextList);
        editor.putString("note/" + noteKey + "/page/" + pageKey + "/text", tabTextList);

        String tabImageList = gson.toJson(this.tabImageList);
        editor.putString("note/" + noteKey + "/page/" + pageKey + "/image", tabImageList);

        editor.apply();
    }

    private void loadData() {
        // create sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("donotforget", MODE_PRIVATE);

        // load arrayList
        Gson gson = new Gson();

        String tabTextList = sharedPreferences.getString("note/" + noteKey + "/page/" + pageKey + "/text", null);
        Type type1 = new TypeToken<ArrayList<String>>() {}.getType();
        this.tabTextList = gson.fromJson(tabTextList, type1);
        if (this.tabTextList == null) {
            this.tabTextList = new ArrayList<>();
        }

        String tabImageList = sharedPreferences.getString("note/" + noteKey + "/page/" + pageKey + "/image", null);
        Type type2 = new TypeToken<ArrayList<_1_Note_3_Tab_2_Rightslide_1_Form>>() {}.getType();
        this.tabImageList = gson.fromJson(tabImageList, type2);
        if (this.tabImageList == null) {
            this.tabImageList = new ArrayList<>();
        }
    }

    // load data
    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
}






