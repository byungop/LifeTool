package kr.chirokyel.lifeTool;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class _1_Note_4_Widget_3_ViewFactory implements RemoteViewsService.RemoteViewsFactory {

    String[] items={""};
    Context context;
    int appWidgetId;

    public _1_Note_4_Widget_3_ViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        System.out.println("viewFactory2");

        SharedPreferences sharedPreferences = context.getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        String memo = sharedPreferences.getString("memo", null);
        items[0] = memo;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout._1_note_4_widget_1_form);
        views.setTextViewText(R.id.widget_form_textview, items[position]);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString(_1_Note_4_Widget_1_Main.EXTRA_WORD, items[position]);
        intent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.widget_form_textview, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
