package ViewPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import DB.DBHelper;
import com.example.semiproject.DBTestActivity;
import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import Communication.SharedObject;
import model.WeatherVO;
import model.alarmVO;

public class FragmentA extends Fragment {
    String TAG="FragmentA";
    View view;
    SharedObject sharedObject;
    BufferedReader bufferedReader;
    Context context;
    TextView fragATV01;
    ArrayAdapter adapter;
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
        context=container.getContext();

        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        Button alarmSetBtn = view.findViewById(R.id.alarmSetBtn);
        final ListView alarmListView = view.findViewById(R.id.alarmListView);


        final DBHelper helper =
                new DBHelper(context, "alarm", 1);
        adapter =
                new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
        alarmListView.setAdapter(adapter);


        alarmSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = String.valueOf(timePicker.getHour());
                String min = String.valueOf(timePicker.getMinute());
                String time = hour +'.'+ min;
                Log.i("test", time);
                helper.insert(time);


                adapter =
                        new ArrayAdapter(context, android.R.layout.simple_list_item_1, helper.getResult());
                alarmListView.setAdapter(adapter);
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
            Log.i("test", weather.getTemp());
        }
        return  view;
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
        Log.v(TAG,"FragmentAonResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"FragmentAonResumeonPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"FragmentAonResumeonStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG,"FragmentAonResumeonDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"FragmentAonResumeonDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG,"FragmentAonResumeonDetach");
    }
}
