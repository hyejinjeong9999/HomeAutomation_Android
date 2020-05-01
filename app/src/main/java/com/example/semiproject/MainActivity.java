package com.example.semiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import RecyclerViewAdapter.ViewType;
import ViewPage.ContentViewPagerAdapter;
import ViewPage.FragmentA;
import ViewPage.FragmentB;
import ViewPage.FragmentHome;
import model.SystemInfoVO;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    RecyclerView recyclerVIew;
    TabLayout tabLayout;
    Context context;
    Bundle bundle;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentHome fragmentHome;
    FragmentA fragmentA;
    FragmentB fragmentB;

    ArrayList<SystemInfoVO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        list.add(new SystemInfoVO(R.drawable.angry,"대기상태","좋음", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angel,"에어컨","꺼짐", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angry,"조명","켜짐", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angel,"냉장고","????", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angry,"현관문","켜짐", ViewType.ItemVertical));

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        fragmentManager = getSupportFragmentManager();

        if (fragmentHome == null) {
            fragmentTransaction=fragmentManager.beginTransaction();
            bundle = new Bundle();
            fragmentHome = new FragmentHome();
            bundle.putSerializable("list", list);
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();
            fragmentHome.setArguments(bundle);
            Log.v(TAG,"fragmentHome==");
        }

        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("홈")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Win")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("게임")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("???")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("???")));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v(TAG,"onTabSelected()_getPosition=="+tab.getPosition());
                fragmentTransaction=fragmentManager.beginTransaction();
                bundle = new Bundle();
                Intent intent = new Intent();
                switch (tab.getPosition()){
                    case 0:
                        if (fragmentHome == null) {
                            fragmentHome = new FragmentHome();
                            Log.v(TAG,"fragmentHome==");
//                            bundle.putParcelableArrayList("list",  (ArrayList<? extends Parcelable>)list);
                        }
                        bundle.putSerializable("list", list);
                        fragmentTransaction.replace(
                                R.id.frame, fragmentHome).commitAllowingStateLoss();
                        fragmentHome.setArguments(bundle);

                        break;
                    case 1:
                        if (fragmentA == null) {
                            fragmentA = new FragmentA();
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentA).commitAllowingStateLoss();
                        fragmentA.setArguments(bundle);
                        break;
                    case 2:
                        if (fragmentB == null) {
                            fragmentB = new FragmentB();
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentB).commitAllowingStateLoss();
                        fragmentB.setArguments(bundle);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View createTabView(String tabName){
        View tabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tvTab = (TextView) tabView.findViewById(R.id.tvTab);
        tvTab.setText(tabName);
//        ImageView ivTab = (ImageView)tabVIew.findViewById(R.id.ivTab);
//        ivTab.setImageResource(tabImage);
        return tabView;
    }
}
