package kr.chirokyel.lifeTool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class _0_Frag3_And_Plan_Todo extends Fragment {

    private FragmentPagerAdapter fragmentPagerAdapter;

    static String fragmentCode2 = "";

    ViewPager viewPager;
    TabLayout tabLayout;

    public _0_Frag3_And_Plan_Todo() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout._0_frag3_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        viewPager = getActivity().findViewById(R.id.frag3_main_viewpager);
        fragmentPagerAdapter = new _0_Frag3_And_Plan_Todo_Adapter(getChildFragmentManager());

        tabLayout = getActivity().findViewById(R.id.frag3_main_tablayout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (fragmentCode2.equals("투두")) {
            viewPager.setCurrentItem(1);
            fragmentCode2 = "";
        }
    }
}
