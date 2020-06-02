package viewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import recyclerViewAdapter.VerticalAdapter;
import model.SystemInfoVO;
import model.SensorDataVO;
import model.WeatherVO;

public class FragmentHome extends Fragment {
    String TAG ="FragmentHome";
    View view;
    Context context;
    VerticalAdapter verticalAdapter;

    ArrayList<SystemInfoVO> list;
    WeatherVO weathers;
    SensorDataVO sensorDataVO;

    GestureDetector gestureDetector;
    communication.SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentHome(communication.SharedObject sharedObject, BufferedReader bufferedReader, SensorDataVO sensorDataVO) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
        this.sensorDataVO = sensorDataVO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmen_home, container, false);

        /**
         * MainActivity 에서 Bundle을 통해 부여된 Key를 입력하고  Data를 받아온다
         */
        context = container.getContext();
        Bundle bundle = getArguments();
        Log.v(TAG,"bundle=="+(ArrayList<SystemInfoVO>)getArguments().get("list"));
        list=(ArrayList<SystemInfoVO>)getArguments().get("list");
        Log.v(TAG,"weathers=="+(WeatherVO) getArguments().get("weather"));
        weathers = (WeatherVO) getArguments().get("weather");
        Log.v(TAG,"weather.getTemp=="+weathers.getTemp());
        sensorDataVO = (SensorDataVO) getArguments().get("sensorData");
        Log.v(TAG,"weather.getOnOff()=="+ sensorDataVO.getWindowStatus());

        /**
         * RecyclerVIew 생성 Code
         */
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewVertical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
        verticalAdapter = new VerticalAdapter(
                context, list, weathers, sharedObject,bufferedReader, sensorDataVO);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(verticalAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"FragmentHomeonResumeonStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"FragmentHomeonResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"FragmentHomeonPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"FragmentHomeonStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG,"FragmentHomeonDestroyView");
    }
}
