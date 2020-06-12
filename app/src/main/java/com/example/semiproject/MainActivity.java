package com.example.semiproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

import event.BackPressCloseHandler;
import viewPage.FragmentAirConditioner;
import communication.SharedObject;
import communication.WeatherService;
import recyclerViewAdapter.ViewType;
import viewPage.FragmentHome;
import viewPage.FragmentLight;

import viewPage.FragmentSetting;
import viewPage.FragmentWindow;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import model.SensorDataVO;
import model.SystemInfoVO;
import model.WeatherVO;


public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    String name = "/ID:ANDROID";

    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthStateListener;
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

    FragmentAirConditioner fragmentAirConditioner;
    FragmentSetting fragmentSetting;
    FragmentLight fragmentLight;
    int fragmentTag = 0;
    ArrayList<SystemInfoVO> list;
    ArrayList<SystemInfoVO> listFragmentWindow;
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
    Thread pattenThread;
    int cntPatten = 0;
    double lastClapTime = 0;
    AudioDispatcher dispatcher;
    PercussionOnsetDetector mPercussionDetector;

    BackPressCloseHandler backPressCloseHandler;

    boolean voiceRecognition;
    private SharedPreferences appData;

    TextToSpeech tts;       //음석 출력관련 변수 선언

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = appData.edit();
        voiceRecognition = appData.getBoolean("VOICE_RECOGNITION", false);

        //RecyclerView Item List 생성성//
        initRecyclerAdapter();
        //Service Start//
        serviceIntent = new Intent(getApplicationContext(), WeatherService.class);
        startService(serviceIntent);
        //Communication Thread Start//
        thread.start();

        //음석인식 변수 정의
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
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
                setCustomView(createTabView(R.drawable.settings_black)));
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
            //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(2000));
        } catch (Exception e) {
            Log.v(TAG, "RecognizerIntent Exception==" + e);
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(recognitionListener);

        //패턴인식 레코그니션 실행
        pattenRecognition(intent);

        //onBackPressed Event 객체 생성
        backPressCloseHandler = new BackPressCloseHandler(this);

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
//                        else if (currentFragment instanceof FragmentSetting){
//                            Log.v(TAG,"FragmentSetting");
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
        list.add(new SystemInfoVO(
                R.drawable.smart, "스마스 모트",  ViewType.ItemVertical));
        list.add(new SystemInfoVO(
                R.drawable.sleep, "수면 모드",  ViewType.ItemVertical));
        list.add(new SystemInfoVO(
                R.drawable.ic_windy, "환기 모드",  ViewType.ItemVertical));
        list.add(new SystemInfoVO(
                R.drawable.outing, "외출 모드",  ViewType.ItemVertical));

        listFragmentWindow = new ArrayList<>();
        listFragmentWindow.add(new SystemInfoVO("대기질상태", ViewType.ItemVerticalAir));
        listFragmentWindow.add(new SystemInfoVO(R.drawable.window1, "대기질컨트롤", ViewType.ItemVerticalAirControl));
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
     * Service 를 이용해 webServer 에서 REST API 통신을 이용 데이터를 가져온다
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.v(TAG, "onNewIntent()_intent.getExtras()==" + intent.getExtras().get("weatherResult").toString());

        weathers = (WeatherVO[]) intent.getExtras().get("weatherResult");
        Log.v(TAG, "onNewIntent()_weathers[0].getTemp()==" + weathers[0].getTemp());
        Log.v(TAG, "onNewIntent()_weathers[0].getPm25Value()==" + weathers[0].getPm25Value());
        weatherVO = weathers[0];
        weatherVO.checkElement();
        // WebServer로 부터 가져온 데이터를 Fragment 를 생성하면서 Fragment 에 데이터를 넘겨준다
        fragmentTransaction = fragmentManager.beginTransaction();
        bundle = new Bundle();
        fragmentHome = new FragmentHome(sharedObject, bufferedReader, sensorDataVO);
        bundle.putSerializable("list", list);
        bundle.putSerializable("weather", weatherVO);
        bundle.putSerializable("sensorData", sensorDataVO);
        Log.v(TAG, "WeatherTEST" + sensorDataVO.getTemp());
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
        Log.v(TAG, "onBackPressed() == IN");
        backPressCloseHandler.onBackPressed();
    }

    /**
     * Server Socket Client Remove
     */
    @Override
    protected void onDestroy() {
        sharedObject.put(name + user.getEmail() + " OUT");
        Log.v(TAG, "onDestroy()");
        try {
            printWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.v(TAG, "onDestroy()_bufferedReader.close()_IOException==" + e.toString());
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
                name = name.trim();
                user = FirebaseAuth.getInstance().getCurrentUser();
                sharedObject.put(name + user.getEmail() + " IN");
                Log.v(TAG, "user name ==" + user.getEmail());

//                sharedObject.put(user.getEmail());

                Log.v(TAG, "name==" + name);
//                    Communication.DataReceveAsyncTask asyncTask =
//                            new Communication.DataReceveAsyncTask(bufferedReader);
//                    asyncTask.execute();
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //sensorDataVO = new SensorDataVO();
                        //fragmentWindow = new FragmentWindow(sharedObject, bufferedReader, sensorDataVO, weatherVO);
                        while (true) {
                            try {
                                jsonData = bufferedReader.readLine();
                                Log.v(TAG, "jsonDataReceive==" + jsonData);
                                if (jsonData != null) {
                                    sensorDataVO = objectMapper.readValue(jsonData, SensorDataVO.class);

                                    bundle.putSerializable("sensorData", sensorDataVO);
                                    //fragmentWindow.setArguments(bundle);
//                                        SensorDataVO vo1 = (SensorDataVO)jsonObject.get(jsonData);
//                                        Log.v(TAG,"jsonObject.get(\"temp\")"+vo1.getTemp());
                                    Log.v(TAG, sensorDataVO.toString());
                                }
                            } catch (IOException e) {
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
            if (swipeRefresh.isEnabled() == false) {
                swipeRefresh.setEnabled(true);
            }
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
                        fragmentWindow = new FragmentWindow(sharedObject, bufferedReader, sensorDataVO, weatherVO);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentWindow).commitAllowingStateLoss();
//                        fragmentWindow.setArguments(bundleFagmentA);
                    bundle.putSerializable("weather", weatherVO);
                    bundle.putSerializable("listFragmentWindow", listFragmentWindow);
                    bundle.putSerializable("sensorData", sensorDataVO);
                    fragmentWindow.setArguments(bundle);
                    tab.setIcon(R.drawable.toys_white_18dp);
//                    tab.setCustomView(createTabView(R.drawable.toys_white_18dp));
                    break;
                case 2:
                    Log.v(TAG, "onTabSelected()_speechRecognizer");
                    if (!dispatcher.isStopped()) {
                        dispatcher.stop();
                        pattenThread.interrupt();
                    }
                    speechRecognizer.startListening(intent);
                    break;
                case 3:
                    if (fragmentAirConditioner == null) {
                        fragmentAirConditioner = new FragmentAirConditioner(sharedObject, bufferedReader);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentAirConditioner).commitAllowingStateLoss();
                    bundle.putSerializable("weather", weatherVO);
                    bundle.putSerializable("sensorData", sensorDataVO);
                    fragmentAirConditioner.setArguments(bundle);
                    fragmentTag = 3;
                    break;
                case 4:
                    if (fragmentSetting == null) {
                        fragmentSetting = new FragmentSetting(
                                sharedObject, bufferedReader);
                    }
                    fragmentTransaction.replace(
                            R.id.frame, fragmentSetting).commitAllowingStateLoss();
                    fragmentSetting.setArguments(bundle);
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
            switch (tab.getPosition()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Log.v(TAG, "onTabSelected()_speechRecognizer");
                    if (!dispatcher.isStopped()) {
                        dispatcher.stop();
                        pattenThread.interrupt();
                    }
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
    public void pattenRecognition(final Intent pattenIntent) {

        dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        double threshold = 6;
        double sensitivity = 25;
        final Handler handler = new Handler();
        final Runnable runn = new Runnable() {
            @Override
            public void run() {
                if (!dispatcher.isStopped()) {
                    dispatcher.stop();
                    pattenThread.interrupt();
                }
                speechRecognizer.startListening(pattenIntent);
            }
        };

        mPercussionDetector = new PercussionOnsetDetector(22050, 1024,
                new OnsetHandler() {
                    @Override
                    public void handleOnset(double time, double salience) {
                        if (voiceRecognition) {
                            Log.v(TAG, "time : " + time + ", salience : " + salience);
                            Log.v(TAG, "Clap detected!");
                            cntPatten++;
                            if (time - lastClapTime < 1 && time - lastClapTime > 0 && cntPatten >= 1) {
                                cntPatten = 0;
                                lastClapTime = 0;
                                handler.post(runn);
                            }
                            lastClapTime = time;
                        }
                    }

                }, sensitivity, threshold);

        dispatcher.addAudioProcessor(mPercussionDetector);
        pattenThread = new Thread(dispatcher, "Audio Dispatcher");
        pattenThread.start();
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        String recogTAG = "RecognitionListener";

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.v(recogTAG, "onReadyForSpeech()");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.v(recogTAG, "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            Log.v(recogTAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.v(recogTAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.v(recogTAG, "onEndOfSpeech()");
        }

        @Override
        public void onError(int i) {
            Log.v(recogTAG, "너무 늦게 말하면 오류뜹니다");
//            Toast.makeText(getApplicationContext(),"다시 말해",Toast.LENGTH_LONG);
//            speechRecognizer.startListening(intent);

//            Toast.makeText(getApplicationContext(),"다시 말해",Toast.LENGTH_LONG);
            //////////////////////////
            cntPatten = 0;
            lastClapTime = 0;
            pattenRecognition(intent);
            Log.v(TAG, "너무 늦게 말하면 오류뜹니다");

        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            Log.v(TAG, "음성인식==" + rs[0]);
            Log.v(TAG, "음성인식size==" + mResult.size());
            String str = "";
            if (rs[0].contains("창문")) {
                if (rs[0].contains("열어")) {
                    str = "창문을 열겠습니다";
                    speech(str);
                    sharedObject.put("/ANDROID>/WINDOW ON");
                } else if (rs[0].contains("닫아")) {
                    str = "창문을 닫겠습니다";
                    speech(str);
                    sharedObject.put("/ANDROID>/WINDOW OFF");
                }
            }else if(rs[0].contains("공기")){
                if(rs[0].contains("켜")){
                    str = "공기청정기를 가동합니다";
                    speech(str);
                    sharedObject.put("/ANDROID>/AIRPURIFIER ON");
                }else  if(rs[0].contains("꺼")){
                    str = "공기청정기 작동을 중지합니다";
                    speech(str);
                    sharedObject.put("/ANDROID>/AIRPURIFIER OFF");
                }
            }
            for (Fragment currentFragment : getSupportFragmentManager().getFragments()) {
                if (currentFragment.isVisible()) {
                    if (currentFragment instanceof FragmentHome) {
                        Log.v(TAG, "FragmentHome");
                        startService(serviceIntent);
                    } else if (currentFragment instanceof FragmentWindow) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        if (fragmentWindow == null) {
                            fragmentWindow = new FragmentWindow(sharedObject, bufferedReader, sensorDataVO, weatherVO);
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

            if (!dispatcher.isStopped()) {
                dispatcher.stop();
                pattenThread.interrupt();
            }
            pattenRecognition(intent);
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
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            /**
             * 가장 위에 표시된 Fragment 를 얻어와(getSupportFragmentManager().getFragments()) 해당 Fragment Refresh
             */
            Log.v(TAG, "onRefresh()_Fragment==" + getSupportFragmentManager().getFragments().toString());
            for (Fragment currentFragment : getSupportFragmentManager().getFragments()) {
                if (currentFragment.isVisible()) {
                    if (currentFragment instanceof FragmentHome) {
                        Log.v(TAG, "FragmentHome");
                        startService(serviceIntent);
                        break;
                    } else if (currentFragment instanceof FragmentWindow) {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        if (fragmentWindow == null) {
                            fragmentWindow = new FragmentWindow(sharedObject, bufferedReader, sensorDataVO, weatherVO);
                        }
                        fragmentTransaction.replace(
                                R.id.frame, fragmentWindow).commitAllowingStateLoss();
                        bundle.putSerializable("weather", weatherVO);
                        bundle.putSerializable("sensorData", sensorDataVO);
                        fragmentWindow.setArguments(bundle);
                        Log.v(TAG, "FragmentA_OnRefreshListener");

                    } else if (currentFragment instanceof FragmentAirConditioner) {
                        Log.v(TAG, "FragmentAirConditioner");
                    } else if (currentFragment instanceof FragmentSetting) {
                        Log.v(TAG, "FragmentSetting");
                    } else if (currentFragment instanceof FragmentLight) {
                        Log.v(TAG, "FragmentLight");
                    }
                }
            }
            swipeRefresh.setRefreshing(false); //false 로 설정해야 새로고침 아이콘이 종료된다
            sharedObject.put("/ANDROID>/REFRESH ON");
        }
    };

    //실제 음성이 말하는 메소드
    private void speech(String msg) {
        tts.setPitch(1.5f); //1.5톤 올려서
        tts.setSpeechRate(1.0f); //1배속으로 읽기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
            // API 20
        else
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }
}
