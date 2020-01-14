package kr.chirokyel.lifeTool;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class _1_Note_4_Widget_2_Service extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        System.out.println("viewFactory1");
        return new _1_Note_4_Widget_3_ViewFactory(this.getApplicationContext(), intent);
    }
}
