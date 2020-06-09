package viewPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import communication.SharedObject;
import model.WeatherVO;

import recyclerViewAdapter.AirRecyclerAdapter;

import model.SensorDataVO;
import model.SystemInfoVO;

public class FragmentWindow extends Fragment {

    private String TAG="FragmentWindow";
    private View view;
    private SharedObject sharedObject;
    private Context context;
    AirRecyclerAdapter airRecyclerAdapter;
    private BufferedReader bufferedReader;

    private SensorDataVO sensorDataVO;
    private WeatherVO weatherVO;
    private ArrayList<SystemInfoVO> list;

    public FragmentWindow(SharedObject sharedObject, BufferedReader bufferedReader, SensorDataVO sensorDataVO, WeatherVO weatherVO) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
        this.sensorDataVO = sensorDataVO;
        this.weatherVO = weatherVO;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=container.getContext();
        view = inflater.inflate(R.layout.fragment_air, container, false);

        Bundle bundle = getArguments();
        list=(ArrayList<SystemInfoVO>)getArguments().get("listFragmentWindow");
        weatherVO = (WeatherVO) getArguments().get("weather");
        sensorDataVO = (SensorDataVO) getArguments().get("sensorData");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAirVertical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
        airRecyclerAdapter = new AirRecyclerAdapter(
                context, sharedObject, bufferedReader, sensorDataVO, weatherVO, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(airRecyclerAdapter);

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
        Log.v(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "FragmentAonResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "FragmentAonResumeonPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "FragmentAonResumeonStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "FragmentAonResumeonDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "FragmentAonResumeonDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG,"FragmentAonResumeonDetach");
    }
}