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

public class _0_Frag2_And_Weather_Step extends Fragment {

    private FragmentPagerAdapter fragmentPagerAdapter;

    static String fragmentCode1 = "";
    static String fragmentCode2 = "";

    ViewPager viewPager;
    TabLayout tabLayout;

    public _0_Frag2_And_Weather_Step() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreate1");
        return inflater.inflate(R.layout._0_frag2_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("onResume1");

       viewPager= getActivity().findViewById(R.id.frag2_main_viewpager);

       fragmentPagerAdapter = new _0_Frag2_And_Weather_Step_Adapter(getChildFragmentManager());

       tabLayout = getActivity().findViewById(R.id.frag2_main_tablayout);
       viewPager.setAdapter(fragmentPagerAdapter);
       tabLayout.setupWithViewPager(viewPager);
        System.out.println(fragmentCode1 + "");


        if (fragmentCode1.equals("스텝")) {
            viewPager.setCurrentItem(1);
            fragmentCode1 = "";
        }

        if (fragmentCode2.equals("북북")) {
            viewPager.setCurrentItem(3);
            fragmentCode1 = "";
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop1");

    }
}
