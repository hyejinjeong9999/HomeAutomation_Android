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
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import model.WeatherVO;

import static android.content.Context.MODE_PRIVATE;

public class FragmentA extends Fragment {
    private String TAG="FragmentA";
    private View view;
    private SharedObject sharedObject;
    private BufferedReader bufferedReader;
    private Context context;
    private RadioGroup grpBtn;
    private TextView fragATV01;
    private ToggleButton toggleBtn;
    private ToggleButton windowToggleButton;
    private TimePicker picker;
    private TextView setTv01;
    private TextView setTv02;
    private Button alarmSetBtn;

    public FragmentA(){

    }
    public FragmentA(SharedObject sharedObject, BufferedReader bufferedReader) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a,container,false);
        assert container != null;
        context=container.getContext();

        // 창문 상태 (자동/수동)


        // fragARadioBtn; 창문 버튼 (자동/수동)
        grpBtn = view.findViewById(R.id.fragARadioGroupBtn);
        grpBtn.check(R.id.autoBtn);     // 일단 자동에 설정
        Log.i("atest", "getChecked: " +(grpBtn.getCheckedRadioButtonId()));
        grpBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Button checkedBtn =  group.findViewById(checkedId);

                switch (checkedId){
                    case R.id.autoBtn:{
                        Toast.makeText(context, "AUTO;  " + checkedBtn.getText(), Toast.LENGTH_SHORT).show();
                        windowToggleButton.setVisibility(View.GONE);
                        picker.setVisibility(View.VISIBLE);
                        alarmSetBtn.setVisibility(View.VISIBLE);
//                        toggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.btn_open));
//                        toggleBtn.setBackgroundResource(R.drawable.btn_open);
                        break;
                    }
                    case R.id.manualBtn:{
                        Toast.makeText(context, "MANUAL;  " + checkedBtn.getText(), Toast.LENGTH_SHORT).show();
                        picker.setVisibility(View.GONE);
                        alarmSetBtn.setVisibility(View.GONE);
                        windowToggleButton.setVisibility(View.VISIBLE);
//                        toggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.btn_close));
//                        toggleBtn.setBackgroundResource(R.drawable.btn_close);
                        break;
                    }
                }
                }
            });


        // fragAToggleBtn; 창문 버튼 (자동/수동)
        /*toggleBtn = view.findViewById(R.id.fragAToggleBtn);
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleBtn.isChecked()){
                   Toast.makeText(context, "모드; 수동", Toast.LENGTH_SHORT).show();
                    picker.setVisibility(View.VISIBLE);
                    windowToggleButton.setVisibility(View.VISIBLE);
                    alarmSetBtn.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(context, "모드; 자동", Toast.LENGTH_SHORT).show();
                    picker.setVisibility(View.GONE);
                    windowToggleButton.setVisibility(View.GONE);
                    alarmSetBtn.setVisibility(View.GONE);
                }
            }
        });*/

        // 창문 수동 열기/닫기
        windowToggleButton = view.findViewById(R.id.windowSwitch);
        windowToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(windowToggleButton.isChecked()){
                    Toast.makeText(context, "닫힘", Toast.LENGTH_SHORT).show();
                    Log.i("atest", "수동: 닫힘");
                }else{
                    Toast.makeText(context, "열림", Toast.LENGTH_SHORT).show();
                    Log.i("atest", "수동: 열림");
                }
            }
        });


        // 현제 온도 보여주기
        fragATV01 = view.findViewById(R.id.fragACurrentTemp);
        Bundle bundle = getArguments();
        if (bundle != null) {
            WeatherVO weather = (WeatherVO) bundle.getSerializable("weather");
            Log.v(TAG,"weather=="+weather);

            fragATV01.setText(weather.getTemp());
            Log.v(TAG,"getTemp=="+weather.getTemp());
        }

        // 알람 시간

        picker = view.findViewById(R.id.timePicker);
        picker.setIs24HourView(false);      // true: 24시간, false: 12시간

        // 최근 설정한 값 or 현재시간
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Daily Alarm", MODE_PRIVATE);
        long mills = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(mills);

        Date nextDate = nextNotifyTime.getTime();
        String date_text = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault()).format(nextDate);
        Toast.makeText(this.context, "다음 알람 " + date_text + "으로 설정", Toast.LENGTH_LONG).show();
        Log.i("atest", "## 01 ##");
        Log.i("atest", "date_text: " + date_text);

        // TimePicker 초기화
        Date currentTime  = nextNotifyTime.getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat minutesFormat = new SimpleDateFormat("mm", Locale.getDefault());
        SimpleDateFormat secondFormat = new SimpleDateFormat("ss", Locale.getDefault());
        int preHour = Integer.parseInt(hourFormat.format(currentTime));
        int preMinute = Integer.parseInt(minutesFormat.format(currentTime));
        int preSecond = Integer.parseInt(secondFormat.format(currentTime));

        //  배려
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

                //  배려
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
//                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
//                editor.putLong("NextNofityTime", (long)calendar.getTimeInMillis());
//                editor.apply();

                // method:diaryNotification
                diaryNotification(calendar);
            }
        });
        return  view;

    }
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
