package kr.chirokyel.lifeTool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class _1_Note_1_Note_1_Form_Layout extends LinearLayout {

    public _1_Note_1_Note_1_Form_Layout(Context context) {
        super(context);
    }

    public _1_Note_1_Note_1_Form_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public _1_Note_1_Note_1_Form_Layout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int width, int height) {
        // note we are applying the width value as the height
        super.onMeasure(width, width);
    }
}
