package ViewPage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.semiproject.AlarmReceiver;
import com.example.semiproject.DeviceBootReceiver;
import com.example.semiproject.R;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import Communication.SharedObject;
import DB.DBHelper;
import model.WeatherVO;
import model.WindowVO;
import model.alarmVO;


public class FragmentWindow extends Fragment {

    ArrayAdapter adapter;
    private String TAG="FragmentWindow";
    private View view;
    private SharedObject sharedObject;
    private BufferedReader bufferedReader;
    private Context context;
    private TextView fragATV01;


    FrameLayout frameLayout;
    ToggleButton tglBtnWindow;
    private ToggleButton toggleBtn;
    private ToggleButton windowToggleButton;
    private TimePicker picker;
    private TextView setTv01;
    private TextView setTv02;
    private Button alarmSetBtn;
    Button btnAuto, btnManual;
    int modeSituation = 0;
    String jsonData;
    ImageButton imageButton;
    ImageView ivWindow;
    WeatherVO weathers;
    WindowVO windowVO;

    public FragmentWindow(SharedObject sharedObject) {
        this.sharedObject = sharedObject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_window,container,false);
        assert container != null;
        context=container.getContext();

        // timePicker or picker??
        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        final ListView alarmListView = view.findViewById(R.id.alarmListView);
        alarmSetBtn = view.findViewById(R.id.alarmSetBtn);

        final DBHelper helper = new DBHelper(
                context, "alarm", 1);
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
        alarmListView.setAdapter(adapter);

        weathers = (WeatherVO) getArguments().get("weather");
        Log.v(TAG,"weather.getTemp=="+weathers.getTemp());
        windowVO = (WindowVO) getArguments().get("window");
        Log.v(TAG,"window.getONOFF=="+windowVO.getOnOff());

        // 창문 상태 체크 (열림/닫힘)
        try {
            if (windowVO.getOnOff().equals("1")){
                Log.v(TAG,"11111111111   OPEn    11111111");
                tglBtnWindow.setBackgroundResource(R.drawable.window2);
            }else {
                Log.v(TAG,"222222222");
                tglBtnWindow.setBackgroundResource(R.drawable.window1);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "서버가 꺼졋나봐;;", Toast.LENGTH_SHORT).show();
        }

        // framyLayout
        frameLayout = view.findViewById(R.id.frameLayout);

        // 상단 LinearLayout btnAuto/Manual
        btnAuto = view.findViewById(R.id.btnAuto);
        btnAuto.setOnClickListener(mClick);
        btnManual = view.findViewById(R.id.btnManual);
        btnManual.setOnClickListener(mClick);

        // 창문 ToggleBtn 수동 열기/닫기
        tglBtnWindow = view.findViewById(R.id.tglBtnWindow);
        tglBtnWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"windowVO.getOnOff()"+windowVO.getOnOff());
                if(tglBtnWindow.isChecked()){
                    Log.v(TAG, "tglBtnWindow.isChecked()"+tglBtnWindow.isChecked());
                    sharedObject.put("/ANDROID>/WINDOWS OFF");
                    tglBtnWindow.setBackgroundResource(R.drawable.window2);
                }else {
                    Log.v(TAG, "tglBtnWindow.isChecked()"+tglBtnWindow.isChecked());
                    sharedObject.put("/ANDROID>/WINDOWS ON");
                    tglBtnWindow.setBackgroundResource(R.drawable.window1);
                }

//                if (tglBtnWindow.isChecked()) {
//                    Toast.makeText(context, "닫히는 중", Toast.LENGTH_SHORT).show();
//                    Log.i("atest", "Checked: 닫기");
//                } else {
//                    Toast.makeText(context, "열리는 중", Toast.LENGTH_SHORT).show();
//                    Log.i("atest", "Unchecked: 열기");
//                }

            }
        });

        //
//        // timePicker by seo
//        alarmSetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String hour = String.valueOf(timePicker.getHour());
//                String min = String.valueOf(timePicker.getMinute());
//                String time = hour +'.'+ min;
//                Log.i("test", time);
//                helper.insert(time);
//
//                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
//                alarmListView.setAdapter(adapter);
//            }
//        });

        /*// alarmSetBtn
        Button alarmSetBtn = view.findViewById(R.id.alarmSetBtn);

        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
        alarmListView.setAdapter(adapter);

        alarmSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = String.valueOf(timePicker.getHour());
                String min = String.valueOf(timePicker.getMinute());
                String time = hour +'.'+ min;
                Log.i("test", time);
                helper.insert(time);

                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
                alarmListView.setAdapter(adapter);
            }
        });*/

        // 알람 시간
        picker = view.findViewById(R.id.timePicker);
        picker.setIs24HourView(false);      // true: 24시간, false: 12시간

        // 최근 설정한 값 or 현재시간
        SharedPreferences sharedPreferences = context.getSharedPreferences("Daily Alarm", Context.MODE_PRIVATE);
        long mills = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(mills);

        Date nextDate = nextNotifyTime.getTime();
        String date_text = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault()).format(nextDate);
        Toast.makeText(context, "현제 시각 " + date_text + " 입니다", Toast.LENGTH_LONG).show();
        Log.i("atest", "## 01 ##");
        Log.i("atest", "date_text: " + date_text);

        // TimePicker 초기화
        Date currentTime = nextNotifyTime.getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat minutesFormat = new SimpleDateFormat("mm", Locale.getDefault());
//        SimpleDateFormat secondFormat = new SimpleDateFormat("ss", Locale.getDefault());
        int preHour = Integer.parseInt(hourFormat.format(currentTime));
        int preMinute = Integer.parseInt(minutesFormat.format(currentTime));
//        int preSecond = Integer.parseInt(secondFormat.format(currentTime));

        //  SDK 23 Marshmallow 배려
        if(Build.VERSION.SDK_INT >= 23){
            picker.setHour(preHour);
            picker.setMinute(preMinute);
        }else{
            picker.setCurrentHour(preHour);
            picker.setCurrentMinute(preMinute);
        }

        alarmSetBtn = view.findViewById(R.id.alarmSetBtn);
        alarmSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, hour_24, minute;
                String am_pm;

                //  SDK 23 Marshmallow 배려
                if(Build.VERSION.SDK_INT >= 23){
                    hour_24 = picker.getHour();
                    minute = picker.getMinute();
                }else{
                    hour_24 = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }

                if (hour_24 > 12) {
                    am_pm = "오후";
                    hour = hour_24 - 12;
                }else {
                    am_pm = "오전";
                    hour = hour_24;
                }

                //지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);   // 필요한가?

                // 이미 지난 시간이면 다음날 같은 시간
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE, 1);
                }

                Date currentDateTime = calendar.getTime();
                String date_text = new SimpleDateFormat("a hh:mm", Locale.getDefault()).format(currentDateTime);
                Toast.makeText(context, "다음 알람 " + date_text + "으로 설정", Toast.LENGTH_LONG).show();
                Log.i("atest", "## 02 ##");
                Log.i("atest", "date_text: " + date_text);

                // Preference 설정 값 저장
                SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit();
                editor.putLong("NextNofityTime", (long)calendar.getTimeInMillis());
                editor.apply();

                // method:diaryNotification
                diaryNotification(calendar);
            }
        });
        return  view;
    }


    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAuto:
                    if (modeSituation == 1){
                        Log.i("atest", "modeSituation" + String.valueOf(modeSituation));
                        btnAuto.setBackgroundResource(R.drawable.win_btn_back_image_check);
                        btnManual.setBackgroundResource(R.drawable.win_btn_back_image);
                        frameLayout.setBackgroundResource(R.drawable.round_button_default);
                        picker.setVisibility(View.VISIBLE);
                        alarmSetBtn.setVisibility(View.VISIBLE);
                        tglBtnWindow.setEnabled(false);
                        picker.setEnabled(true);
                        modeSituation = 0;
                        Log.v(TAG,"modeSituation_onClick()=="+btnAuto.getText());
                        Toast.makeText(context, "모드; 자동", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnManual:
                    if (modeSituation == 0){
                        Log.i("atest", "modeSituation" + String.valueOf(modeSituation));
                        btnManual.setBackgroundResource(R.drawable.win_btn_back_image_check);
                        btnAuto.setBackgroundResource(R.drawable.win_btn_back_image);
                        frameLayout.setBackgroundResource(R.drawable.round_button);
//                      picker.setVisibility(View.GONE);
                        tglBtnWindow.setEnabled(true);
                        picker.setEnabled(false);
                        alarmSetBtn.setVisibility(View.GONE);
                        modeSituation = 1;
                        Log.v(TAG,"modeSituation_onClick()=="+btnManual.getText());
                        Toast.makeText(context, "모드; 수동", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    // Noti띄우기
    private void  diaryNotification(Calendar calendar){
        Boolean dailyNotify = true;     //  항상 알람 사용

        PackageManager pm = this.getActivity().getPackageManager();
        // 재부팅 후에도 알람이 작동하기 위해
        ComponentName receiver = new ComponentName(getActivity(), DeviceBootReceiver.class);     // 여기까지
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (windowVO.getOnOff().equals("1")){
            Log.v(TAG,"11111111111   OPEn    11111111");
            tglBtnWindow.setBackgroundResource(R.drawable.window2);
        }else if(windowVO.getOnOff().equals("2")){
            Log.v(TAG,"222222222");
            tglBtnWindow.setBackgroundResource(R.drawable.window1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG,"onDetach");
    }
}