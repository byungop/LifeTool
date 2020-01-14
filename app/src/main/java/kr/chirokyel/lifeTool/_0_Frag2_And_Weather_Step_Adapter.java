package kr.chirokyel.lifeTool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class _0_Frag2_And_Weather_Step_Adapter extends FragmentPagerAdapter {
    public _0_Frag2_And_Weather_Step_Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 :
                return new _2_Weather_3_Main_Fragment();
            case 1 :
                return new _3_Step_3_Main_Fragment();
            case 2 :
                return new _8_Subway_1_Main_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "날씨";
            case 1 :
                return "걸음";
            case 2 :
                return "기타";
            default:
                return null;
        }
    }
}
