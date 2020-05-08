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
import java.io.ObjectInputStream;
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
import model.TestVO;
import model.WeatherVO;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    RecyclerView recyclerVIew;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    Bundle bundle;
    TestVO testVO = new TestVO();
    WeatherVO[] weathers;

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
    ObjectInputStream objectInputStream;
    Communication.SharedObject sharedObject = new Communication.SharedObject();

    // recycler_item_weatherinfo 관련
    TextView roomTemp;
    ImageView outWeather;
    ImageView roomPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RecyclerView Item List 생성성
        initRecyclerAdapter();
        //Service Start
        Intent i = new Intent(getApplicationContext(), WeatherService.class);
        startService(i);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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
                    socket = new Socket("70.12.60.98", 1357);
                    bufferedReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    printWriter = new PrintWriter(socket.getOutputStream());
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Log.v(TAG, "Socket Situation==" + socket.isConnected());
//                    Communication.DataReceveAsyncTask asyncTask =
//                            new Communication.DataReceveAsyncTask(bufferedReader);
//                    asyncTask.execute();
                    while (true) {
                        String msg = sharedObject.pop();
                        printWriter.println(msg);
                        printWriter.flush();
                    }
                } catch (IOException e) {
                    Log.v(TAG, "Socket Communication IOException==" + e);
                }
            }
        });
        thread.start();
//        Communication.DataReceveAsyncTask111 asyncTaskTest =
//                new Communication.DataReceveAsyncTask111(objectInputStream, testVO);
//        asyncTaskTest.execute();

        ///ObjectBuffer
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (objectInputStream != null) {
                        try {
                            testVO = (TestVO) objectInputStream.readObject();
                            Log.i("test", testVO.getTemp1());
                            Log.v(TAG, "onCreate==" + testVO.toString());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.v(TAG, "nulllllllll");
                    }
                }
            }
        });
//        thread1.start();

        /**
         * App 실행시 처음 표시해줄 Fragment
         * 선언해 주지 않으면 MainActivity 의 빈 화면이 보이게 된다
         */
        fragmentManager = getSupportFragmentManager();
        if (fragmentHome == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            bundle = new Bundle();
            fragmentHome = new FragmentHome(sharedObject, bufferedReader);
            bundle.putSerializable("list", list);
//            bundle.putSerializable("weather", weathers[0]);
            fragmentHome.setArguments(bundle);
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();

            Log.v(TAG, "fragmentHome==");
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
                Log.v(TAG, "onTabSelected()_getPosition==" + tab.getPosition());
                fragmentTransaction = fragmentManager.beginTransaction();
//                bundle = new Bundle();
                switch (tab.getPosition()) {
                    case 0:
                        if (fragmentHome == null) {
                            fragmentHome = new FragmentHome(sharedObject, bufferedReader);
                            Log.v(TAG, "fragmentHome==");
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentHome).commitAllowingStateLoss();
                        bundle.putSerializable("list", list);
                        bundle.putSerializable("weather", weathers[0]);
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
                        if (fragmentTest == null) {
                            fragmentTest = new FragmentTest(sharedObject, bufferedReader);
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentTest).commitAllowingStateLoss();
                        fragmentTest.setArguments(bundle);
                        fragmentTag = 3;
                        break;
                    case 4:
                        if (fragmentLight == null) {
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
                Log.v(TAG, "onTabUnselected()_tab==" + tab.getPosition());
            }

            //텝이 다시 선택되었을 때 호출
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.v(TAG, "onTabReselected()_tab==" + tab.getPosition());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fragmentTag != 0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();
            bundle.putSerializable("list", list);
            fragmentHome.setArguments(bundle);
            fragmentTag = 0;

        } else {
            super.onBackPressed();
        }
    }

    /**
     * FragmentHome의  RecyclerView에 표시할 데이터 정보 Method
     */
    public void initRecyclerAdapter() {
        list = new ArrayList<>();
        list.add(new SystemInfoVO(R.drawable.angry, "대기상태", "좋음", ViewType.ItemVerticalWeather));
        list.add(new SystemInfoVO(R.drawable.angel, "에어컨", ViewType.ItemVerticalSwitch));
        list.add(new SystemInfoVO(R.drawable.angry, "조명", "켜짐", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angel, "냉장고", "????", ViewType.ItemVertical));
        list.add(new SystemInfoVO(R.drawable.angry, "현관문", "켜짐", ViewType.ItemVertical));
    }

    /**
     * 인자를 받아 Custom TabLayout 생성하는 Method
     *
     * @param iconImage
     * @return
     */
    private View createTabView(int iconImage) {
        View tabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
//        TextView tvTab = (TextView) tabView.findViewById(R.id.tvTab);
//        tvTab.setText(tabName);
        ImageView ivTab = (ImageView) tabView.findViewById(R.id.ivTab);
        ivTab.setImageResource(iconImage);
        return tabView;
    }

    /**
     * Service를 이요해 webServer에서 REST API 통신을 이용해 데이터를 가져온다
     */
    Bundle bundleFagmentA;

    @Override
    protected void onNewIntent(Intent intent) {

        Log.i("test", "야2");

        weathers = (WeatherVO[]) intent.getExtras().get("weatherResult");
        roomTemp = findViewById(R.id.tvTemp);
        outWeather = findViewById(R.id.ivWeather);
        roomPM = findViewById(R.id.ivSituation);

        Log.i("test", weathers[0].getTemp());       //tvTemp
        Log.i("test", weathers[0].getWeather());    //ivWeather
        Log.i("test", weathers[0].getHumidity());   //ivSituation
        bundleFagmentA = new Bundle();
        if(bundleFagmentA != null){
            Log.i("test", weathers[0].getTemp());       //tvTemp
            Log.i("test", weathers[0].getWeather());    //ivWeather
            Log.i("test", weathers[0].getHumidity());
            roomTemp.setText(weathers[0].getTemp());
//            outWeather.setImageResource(weathers[0].getWeather());
//            roomPM;
        }
        bundleFagmentA.putSerializable("weather", weathers[0]);
        Log.i("test", "야3");
        super.onNewIntent(intent);
    }
//
//    view = inflater.inflate(R.layout.fragment_a,container,false);
//    context=container.getContext();
//
//    // 현제 온도 보여주기
//    fragATV01 = view.findViewById(R.id.fragACurrentTemp);
//    Bundle bundle = getArguments();
//        if (bundle != null) {
//        WeatherVO weather = (WeatherVO) bundle.getSerializable("weather");
//        fragATV01.setText(weather.getTemp());
//    }
//        return  view;
}