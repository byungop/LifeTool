package kr.chirokyel.lifeTool;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


public class _1_Note_4_Widget_1_Main extends AppWidgetProvider {

    public static String EXTRA_WORD= "com.commonsware.android.appwidget.lorem.WORD";


    // 메서드 : 위젯 업데이트(2/2)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // 위젯 만들기
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout._1_note_4_widget_2_main);

        // 인텐트 : 위젯과 어댑터 서비스 연결
        Intent memoIntent = new Intent(context, _1_Note_4_Widget_2_Service.class);
        memoIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        memoIntent.setData(Uri.parse(memoIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_main_listview, memoIntent);

        // 인텐트 : 위젯 누르면 메모 액티비티로 이동
        Intent clickIntent = new Intent(context, _0_Frag1_All.class);
        clickIntent.putExtra("position", appWidgetId);
        clickIntent.putExtra("slideCode", "open");
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_main_listview, clickPendingIntent);

        // 메서드 : 변경하사항 적용
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_main_listview);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    // 메서드 : 위젯 업데이트(1/2)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    @Override public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // 리시버
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // 액티비티로부터 펜딩인텐트 받으면 위젯 전부 업데이트
        if (intent.getAction().equals("update")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), _1_Note_4_Widget_1_Main.class);
            int[] appWigetIDS = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, appWigetIDS);
        }
    }
}

