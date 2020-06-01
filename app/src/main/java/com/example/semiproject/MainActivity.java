package com.example.semiproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import Communication.SharedObject;
import Communication.WeatherService;
import RecyclerViewAdapter.ViewType;
import ViewPage.FragmentHome;
import ViewPage.FragmentLight;
import ViewPage.FragmentRefrigerator;
import ViewPage.FragmentTest;
import ViewPage.FragmentWindow;
import model.SystemInfoVO;
import model.WeatherVO;
import model.SensorDataVO;


public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    String name = "/ID:ANDROID";

    RecyclerView recyclerVIew;
    ViewPager viewPager;
    TabLayout tabLayout;
    FrameLayout flFirstVIew;
    FrameLayout frame;

    Intent intent;
    Intent serviceIntent;
    Bundle bundle;
    SensorDataVO sensorDataVO = new SensorDataVO();
    WeatherVO weatherVO;
    WeatherVO[] weathers;
    //Fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentHome fragmentHome;
    FragmentWindow fragmentWindow;
    FragmentRefrigerator fragmentRefrigerator;
    FragmentTest fragmentTest;
    FragmentLight fragmentLight;
    int fragmentTag = 0;
    ArrayList<SystemInfoVO> list;
    //Socket Communication
    Socket socket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    ObjectMapper objectMapper = new ObjectMapper();

    SharedObject sharedObject = new SharedObject();

    String jsonData;
    //Speech recognition
    SpeechRecognizer speechRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    // recycler_item_weatherinfo 관련
    TextView roomTemp;
    ImageView outWeather;
    ImageView roomPM;

    SwipeRefreshLayout swipeRefresh;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView Item List 생성성//
        initRecyclerAdapter();
        //Service Start//
        serviceIntent = new Intent(getApplicationContext(), WeatherService.class);
        startService(serviceIntent);
        //Communication Thread Start//
        thread.start();
        /**
         * Implementing Pull to Refresh
         * WeatherService Restart
         */
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(onRefreshListener);

        /**
         * App 실행시 처음 표시해줄 Fragment
         * 선언해 주지 않으면 MainActivity 의 빈 화면이 보이게 된다
         */
        fragmentManager = getSupportFragmentManager();

        if (fragmentHome == null) {
            weatherVO = new WeatherVO();
            fragmentTransaction = fragmentManager.beginTransaction();
            bundle = new Bundle();
            fragmentHome = new FragmentHome(sharedObject, bufferedReader, sensorDataVO);
            bundle.putSerializable("list", list);
            bundle.putSerializable("weather", weatherVO);
            bundle.putSerializable("sensorData", sensorDataVO);
            fragmentHome.setArguments(bundle);
            fragmentTransaction.replace(
                    R.id.frame, fragmentHome).commitAllowingStateLoss();
        }
        /**
         * //TabLayout 항목 추가(추가 항목 수에따라 TabLayout 항목이 생성)
         * TabLayout SelectListenerEvent
         */
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().
                setCustomView(createTabView(R.drawable.house_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().
                setCustomView(createTabView(R.drawable.toys_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().
                setCustomView(createTabView(R.drawable.voice_black)));
        tabLayout.addTab(tabLayout.newTab().
                setCustomView(createTabView(R.drawable.ic_windy)));
        tabLayout.addTab(tabLayout.newTab().
                setCustomView(createTabView(R.drawable.kitchen_black_18dp)));
        tabLayout.addOnTabSelectedListener(mTabSelect);

        /**
         * //////////////////Speech recognition/////////////////////
         */
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECORD_AUDIO)) {
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
//                );
//            }
//        }

        try {
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        }catch (Exception e){
            Log.v(TAG,"RecognizerIntent Exception=="+e);
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(recognitionListener);


        //        frame=findViewById(R.id.frame);
//        frame.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
//            public void onSwipeTop() {
//                Log.v(TAG,"onSwipeTop()");
//                speechRecognizer.startListening(intent);
//            }
//            public void onSwipeRight() {
//                Log.v(TAG,"onSwipeRight()");
//            }
//            public void onSwipeLeft() {
//                Log.v(TAG,"onSwipeLeft()");
//            }
//            public void onSwipeBottom() {
//                Log.v(TAG,"onSwipeBottom()");
//                Log.v(TAG,"onRefresh()_Fragment=="+getSupportFragmentManager().getFragments().toString());
//                for (Fragment currentFragment : getSupportFragmentManager().getFragments()) {
//                    if (currentFragment.isVisible()) {
//                        if(currentFragment instanceof FragmentHome){
//                            Log.v(TAG,"FragmentHome");
//                            startService(serviceIntent);
//                        }else if (currentFragment instanceof FragmentWindow){
//                            fragmentTransaction = fragmentManager.beginTransaction();
//                            if (fragmentWindow == null) {
//                                fragmentWindow = new FragmentWindow(sharedObject);
//                            }
//                            fragmentTransaction.replace(
//                                    R.id.frame, fragmentWindow).commitAllowingStateLoss();
//                            bundle.putSerializable("weather", weatherVO);
//                            bundle.putSerializable("window", windowVO);
//                            fragmentWindow.setArguments(bundle);
//                            Log.v(TAG,"FragmentWindow_OnRefreshListener");
//                        }
//                        else if (currentFragment instanceof FragmentRefrigerator){
//                            Log.v(TAG,"FragmentRefrigerator");
//                        }
//                        else if (currentFragment instanceof FragmentTest){
//                            Log.v(TAG,"FragmentTest");
//                        }
//                        else if (currentFragment instanceof FragmentLight){
//                            Log.v(TAG,"FragmentLight");
//                        }
//                    }
//                }
//            }
//        });
        //ViewPager Code//
//        viewPager = findViewById(R.id.viewPager);
//        ContentViewPagerAdapter pagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout=findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);

//        Communication.DataReceveAsyncTask111 asyncTaskTest =
//                new Communication.DataReceveAsyncTask111(objectInputStream, windowVO);
//        asyncTaskTest.execute();


    }

    /**
     * FragmentHome의  RecyclerView에 표시할 데이터 정보 Method
     */
    public void initRecyclerAdapter() {
        list = new ArrayList<>();
        list.add(new SystemInfoVO(
                R.drawable.angry, "대기상태", "좋음", ViewType.ItemVerticalWeather));
//        list.add(new SystemInfoVO(
//                R.drawable.window1, "창문", ViewType.ItemVerticalSwitch));
        list.add(new SystemInfoVO(
                R.drawable.smart, "SMART MODE", "", ViewType.ItemVertical));
        list.add(new SystemInfoVO(
                R.drawable.sleep, "SLEEP MODE", "", ViewType.ItemVertical));
        list.add(new SystemInfoVO(
                R.drawable.outing, "OUTING MODE", "", ViewType.ItemVertical));
    }

    /**
     * 인자를 받아 Custom TabLayout 생성하는 Method
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
     * Service 를 이용해 webServer 에서 REST API 통신을 이용 데이터를 가져온다
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.v(TAG,"onNewIntent()_intent.getExtras()=="+intent.getExtras().get("weatherResult").toString());

        weathers = (WeatherVO[]) intent.getExtras().get("weatherResult");
        Log.v(TAG,"onNewIntent()_weathers[0].getTemp()=="+weathers[0].getTemp());
        Log.v(TAG,"onNewIntent()_weathers[0].getPm25Value()=="+weathers[0].getPm25Value());
        weatherVO = weathers[0];
        // WebServer로 부터 가져온 데이터를 Fragment 를 생성하면서 Fragment 에 데이터를 넘겨준다
        fragmentTransaction = fragmentManager.beginTransaction();
        bundle = new Bundle();
        fragmentHome = new FragmentHome(sharedObject, bufferedReader, sensorDataVO);
        bundle.putSerializable("list", list);
        bundle.putSerializable("weather", weatherVO);
        bundle.putSerializable("sensorData", sensorDataVO);
        Log.v(TAG,"WeatherTEST"+sensorDataVO.getTemp());
        fragmentHome.setArguments(bundle);
        fragmentTransaction.replace(
                R.id.frame, fragmentHome).commitAllowingStateLoss();
        super.onNewIntent(intent);
    }

    /**
     * BackButton Pressed
     */
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
     * Server Socket Client Remove
     */
    @Override
    protected void onDestroy() {
        sharedObject.put(name+" OUT");
        Log.v(TAG,"onDestroy()");
        try {
            printWriter.close();
            Log.i("mainTest", "printWriter.close();");
            bufferedReader.close();
            Log.i("mainTest", "bufferedReader.close();");
        } catch (IOException e) {
            Log.v(TAG,"onDestroy()_bufferedReader.close()_IOException=="+e.toString());
        }
        super.onDestroy();
    }
    /**
     * Socket Communication witA Server
     */
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                socket = new Socket("70.12.60.98", 1357);
                bufferedReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream());

                Log.v(TAG, "Socket Situation==" + socket.isConnected());
                name=name.trim();
                sharedObject.put(name+" IN");
                Log.v(TAG,"name=="+name);
//                    Communication.DataReceveAsyncTask asyncTask =
//                            new Communication.DataReceveAsyncTask(bufferedReader);
//                    asyncTask.execute();
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sensorDataVO = new SensorDataVO();
                        fragmentWindow = new FragmentWindow(sharedObject);
                        while (true) {
                            try {
                                jsonData = bufferedReader.readLine();
//                                    Log.v(TAG,"jsonDataReceive=="+jsonData);
                                if(jsonData != null){
                                    sensorDataVO =objectMapper.readValue(jsonData, SensorDataVO.class);
                                    Log.v(TAG,"testVo.getTemp=="+ sensorDataVO.getTemp());
                                    Log.v(TAG,"testVo.getLight=="+ sensorDataVO.getAirconditionerStatus());
                                    Log.v(TAG,"testVo.getDustDensity=="+ sensorDataVO.getDustDensity());
                                    Log.v(TAG,"testVo.getOnOff=="+ sensorDataVO.getWindowStatus());

                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    String temp = jsonObject.getString("temp");
                                    Log.v(TAG,"jsonObject_getTemp=="+temp);

                                    bundle.putSerializable("sensorData", sensorDataVO);
                                    fragmentWindow.setArguments(bundle);
//                                        WindowVO vo1 = (WindowVO)jsonObject.get(jsonData);
//                                        Log.v(TAG,"jsonObject.get(\"temp\")"+vo1.getTemp());
                                }
                            }catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread1.start();
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
     //*************************** EventListener ***************************//
    /**
     * TabLayout SelectListenerEvent
     * Fragment Call
     */
    TabLayout.OnTabSelectedListener mTabSelect = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.v(TAG, "onTabSelected()_getPosition==" + tab.getPosition());
            fragmentTransaction = fragmentManager.beginTransaction();
            //FragmentWindow 에 TimePicker Component 가 swipeRefresh 떄문에 이벤트 터치가 겹처 작동하지 않아 해당 Fragment 에서는 비활성
            if(swipeRefresh.isEnabled() == false){
                swipeRefresh.setEnabled(true);
            };
            switch (tab.getPosition()) {
                case 0:
                    if (fragmentHome == null) {
                        fragmentHome = new FragmentHome(sharedObject, bufferedReader, sensorDataVO);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentHome).commitAllowingStateLoss();
                    fragmentHome.setArguments(bundle);
                    fragmentTag = 0;
                    break;
                case 1:
                    swipeRefresh.setEnabled(false);
                    if (fragmentWindow == null) {
                        fragmentWindow = new FragmentWindow(sharedObject);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentWindow).commitAllowingStateLoss();
//                        fragmentWindow.setArguments(bundleFagmentA);
                    bundle.putSerializable("weather", weatherVO);
                    bundle.putSerializable("sensorData", sensorDataVO);
                    fragmentWindow.setArguments(bundle);
                    tab.setIcon(R.drawable.toys_white_18dp);

//                    tab.setCustomView(createTabView(R.drawable.toys_white_18dp));
                    break;
                case 2:
                    Log.v(TAG,"onTabSelected()_speechRecognizer");
                    speechRecognizer.startListening(intent);
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
                    if (fragmentRefrigerator == null) {
                        fragmentRefrigerator = new FragmentRefrigerator(
                                sharedObject,bufferedReader);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentRefrigerator).commitAllowingStateLoss();
                    fragmentRefrigerator.setArguments(bundle);
                    fragmentTag = 2;
//                    if (fragmentLight == null) {
//                        fragmentLight = new FragmentLight(sharedObject,bufferedReader);
//                    }
//                    fragmentTransaction.replace(
//                            R.id.frame, fragmentLight).commitAllowingStateLoss();
//                    fragmentTag = 4;
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
            switch (tab.getPosition()){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Log.v(TAG,"onTabSelected()_speechRecognizer");
                    speechRecognizer.startListening(intent);
                    break;
                case 3:
                    break;
                case 4:

            }
        }
    };

    /**
     * * Speech recognition
     */
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.v(TAG,"onReadyForSpeech()");
        }
        @Override
        public void onBeginningOfSpeech() {
        }
        @Override
        public void onRmsChanged(float v) {
        }
        @Override
        public void onBufferReceived(byte[] bytes) {
        }
        @Override
        public void onEndOfSpeech() {
            Log.v(TAG,"onEndOfSpeech()");
        }
        @Override
        public void onError(int i) {
            Log.v(TAG,"너무 늦게 말하면 오류뜹니다");
            Toast.makeText(getApplicationContext(),"다시 말해",Toast.LENGTH_LONG);
            //////////////////////////
            speechRecognizer.startListening(intent);
        }
        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            Log.v(TAG,"음성인식=="+rs[0]);
            Log.v(TAG,"음성인식size=="+mResult.size());
            if(rs[0].contains("창문")){
                if(rs[0].contains("열어")){
                    sharedObject.put("/ANDROID>/WINDOWS ON");
                }else if(rs[0].contains("닫아")){
                    sharedObject.put("/ANDROID>/WINDOWS OFF");
                }
                for (Fragment currentFragment : getSupportFragmentManager().getFragments()) {
                    if (currentFragment.isVisible()) {
                        if (currentFragment instanceof FragmentHome) {
                            Log.v(TAG, "FragmentHome");
                            startService(serviceIntent);
                        } else if(currentFragment instanceof FragmentWindow) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            if (fragmentWindow == null) {
                                fragmentWindow = new FragmentWindow(sharedObject);
                            }
                            fragmentTransaction.replace(
                                    R.id.frame, fragmentWindow).commitAllowingStateLoss();
                            bundle.putSerializable("weather", weatherVO);
                            bundle.putSerializable("sensorData", sensorDataVO);
                            fragmentWindow.setArguments(bundle);
                            Log.v(TAG, "FragmentA_OnRefreshListener");
                        }
                    }
                }
            }
        }
        @Override
        public void onPartialResults(Bundle bundle) {
        }
        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    /**
     * ReFreshListener
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {
            /**
             * 가장 위에 표시된 Fragment 를 얻어와(getSupportFragmentManager().getFragments()) 해당 Fragment Refresh
             */
            Log.v(TAG,"onRefresh()_Fragment=="+getSupportFragmentManager().getFragments().toString());
            for (Fragment currentFragment : getSupportFragmentManager().getFragments()) {
                if (currentFragment.isVisible()) {
                    if(currentFragment instanceof FragmentHome){
                        Log.v(TAG,"FragmentHome");
                        startService(serviceIntent);
                        break;
                    }else if (currentFragment instanceof FragmentWindow){
                        fragmentTransaction = fragmentManager.beginTransaction();
                        if (fragmentWindow == null) {
                            fragmentWindow = new FragmentWindow(sharedObject);
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentWindow).commitAllowingStateLoss();
                        bundle.putSerializable("weather", weatherVO);
                        bundle.putSerializable("sensorData", sensorDataVO);
                        fragmentWindow.setArguments(bundle);
                        Log.v(TAG,"FragmentA_OnRefreshListener");
                    } else if (currentFragment instanceof FragmentRefrigerator){
                        Log.v(TAG,"FragmentRefrigerator");
                    } else if (currentFragment instanceof FragmentTest){
                        Log.v(TAG,"FragmentTest");
                    } else if (currentFragment instanceof FragmentLight){
                        Log.v(TAG,"FragmentLight");
                    }
                }
            }
            swipeRefresh.setRefreshing(false); //false 로 설정해야 새로고침 아이콘이 종료된다
        }
    };
}
