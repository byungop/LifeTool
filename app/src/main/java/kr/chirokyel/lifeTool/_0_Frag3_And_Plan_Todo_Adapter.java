package kr.chirokyel.lifeTool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class _0_Frag3_And_Plan_Todo_Adapter extends FragmentPagerAdapter {
    public _0_Frag3_And_Plan_Todo_Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 :
                return new _4_Plan_3_Main_Fragment();
            case 1 :
                return new _5_Todo_3_Main_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "계획";
            case 1 :
                return "할일";
            default:
                return null;
        }
    }
}
