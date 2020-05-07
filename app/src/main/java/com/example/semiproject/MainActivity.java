package com.example.semiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import Communication.WeatherService;
import RecyclerViewAdapter.ViewType;
import ViewPage.FragmentA;
import ViewPage.FragmentLight;
import ViewPage.FragmentRefrigerator;
import ViewPage.FragmentHome;
import ViewPage.FragmentTest;
import model.SystemInfoVO;
import model.WeatherVO;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    RecyclerView recyclerVIew;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    Bundle bundle;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentHome fragmentHome;
    FragmentA fragmentA;
    FragmentRefrigerator fragmentRefrigerator;
    FragmentTest fragmentTest;
    FragmentLight fragmentLight;
    int fragmentTag = 0;
    ArrayList<SystemInfoVO> list;

    Socket socket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    Communication.SharedObject sharedObject = new Communication.SharedObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerAdapter();
        Intent i =  new Intent(getApplicationContext(), WeatherService.class);
        startService(i);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        //ViewPager Code//
//        viewPager = findViewById(R.id.viewPager);
//        ContentViewPagerAdapter pagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout=findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
        /**
         * SocketCommunication with Server
         */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket("70.12.60.98",4444);
                    bufferedReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    printWriter = new PrintWriter(socket.getOutputStream());
                    Log.v(TAG,"Socket Situation=="+socket.isConnected());
//                    Communication.DataReceveAsyncTask asyncTask =
//                            new Communication.DataReceveAsyncTask(bufferedReader);
//                    asyncTask.execute();
                    while (true){
                        String msg = sharedObject.pop();
                        printWriter.println(msg);
                        printWriter.flush();
                    }
                }catch (IOException e){
                    Log.v(TAG,"Socket Communication IOException=="+ e);
                }
            }
        });
        thread.start();

        /**
         * App 실행시 처음 표시해줄 Fragment
         * 선언해 주지 않으면 MainActivity 의 빈 화면이 보이게 된다
         */
        fragmentManager = getSupportFragmentManager();
        if (fragmentHome == null) {
            fragmentTransaction=fragmentManager.beginTransaction();
            bundle = new Bundle();
            fragmentHome = new FragmentHome(sharedObject,bufferedReader);
            bundle.putSerializable("list", list);
            fragmentHome.setArguments(bundle);
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();

            Log.v(TAG,"fragmentHome==");
        }
        /**
         * //TabLayout 항목 추가(추가 항목 수에따라 TabLayout 항목이 생성)
         */
//        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("HOME",R.drawable.house_black_18dp)));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Win",R.drawable.toys_black_18dp)));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("냉장고",R.drawable.kitchen_black_18dp)));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("현관문",R.drawable.border_vertical_black_18dp)));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("조명",R.drawable.incandescent_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(R.drawable.house_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(R.drawable.toys_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(R.drawable.kitchen_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(R.drawable.border_vertical_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(R.drawable.incandescent_black_18dp)));
        /**
         * TabLayout SelectListenerEvent
         * Fragment Call
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //텝이 선택 되었을때 호출
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v(TAG,"onTabSelected()_getPosition=="+tab.getPosition());
                fragmentTransaction=fragmentManager.beginTransaction();
//                bundle = new Bundle();
                switch (tab.getPosition()){
                    case 0:
                        if (fragmentHome == null) {
                            fragmentHome = new FragmentHome(sharedObject,bufferedReader);
                            Log.v(TAG,"fragmentHome==");
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentHome).commitAllowingStateLoss();
                        bundle.putSerializable("list", list);
                        fragmentHome.setArguments(bundle);
                        fragmentTag = 0;
                        break;
                    case 1:
                        if (fragmentA == null) {
                            fragmentA = new FragmentA(bufferedReader);
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentA).commitAllowingStateLoss();

                        fragmentA.setArguments(bundleFagmentA);

                        break;
                    case 2:
                        if (fragmentRefrigerator == null) {
                            fragmentRefrigerator = new FragmentRefrigerator();
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentRefrigerator).commitAllowingStateLoss();
                        fragmentRefrigerator.setArguments(bundle);
                        fragmentTag = 2;
                        break;
                    case 3:
                        if(fragmentTest == null){
                            fragmentTest = new FragmentTest(sharedObject,bufferedReader);
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentTest).commitAllowingStateLoss();
                        fragmentTest.setArguments(bundle);
                        fragmentTag = 3;
                        break;
                    case 4:
                        if (fragmentLight == null){
                            fragmentLight = new FragmentLight();
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentLight).commitAllowingStateLoss();
                        fragmentTag = 4;
                }
            }
            //텝이 선택되지 않았을 때 호출
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.v(TAG,"onTabUnselected()_tab=="+tab.getPosition());
            }
            //텝이 다시 선택되었을 때 호출
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.v(TAG,"onTabReselected()_tab=="+tab.getPosition());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fragmentTag != 0){
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();
            bundle.putSerializable("list", list);
            fragmentHome.setArguments(bundle);
            fragmentTag = 0;

        }else {
            super.onBackPressed();
        }
    }

    /**
     * FragmentHome의  RecyclerView에 표시할 데이터 정보 Method
     */
    public void initRecyclerAdapter(){
        list = new ArrayList<>();
        list.add(new SystemInfoVO(R.drawable.angry,"대기상태","좋음", ViewType.ItemVerticalWeather));
        list.add(new SystemInfoVO(R.drawable.angel,"에어컨", ViewType.ItemVerticalSwitch));
        list.add(new SystemInfoVO(R.drawable.angry,"조명","켜짐", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angel,"냉장고","????", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angry,"현관문","켜짐", ViewType.ItemVertical));
    }

    /**
     * 인자를 받아 Custom TabLayout 생성하는 Method
     * @param iconImage
     * @return
     */
    private View createTabView(int iconImage){
        View tabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        TextView tvTab = (TextView) tabView.findViewById(R.id.tvTab);
//        tvTab.setText(tabName);
        ImageView ivTab = (ImageView) tabView.findViewById(R.id.ivTab);
        ivTab.setImageResource(iconImage);
        return tabView;
    }

    Bundle bundleFagmentA;
    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("test", "야2");
        WeatherVO[] weathers = (WeatherVO[]) intent.getExtras().get("weatherResult");
        Log.i("test", weathers[0].getTemp());
        bundleFagmentA = new Bundle();
        bundleFagmentA.putSerializable("weather", weathers[0]);
        Log.i("test", "야3");
        super.onNewIntent(intent);
    }
}
