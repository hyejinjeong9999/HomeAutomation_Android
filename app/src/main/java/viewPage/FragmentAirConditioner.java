package viewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;

import java.io.BufferedReader;

import communication.SharedObject;
import model.SensorDataVO;
import model.WeatherVO;

public class FragmentAirConditioner extends Fragment {
    String TAG="FragmentAirConditioner";
    View view;
    Context context;

    SharedObject sharedObject;
    BufferedReader bufferedReader;

    WeatherVO weatherVO;
    SensorDataVO sensorDataVO;

    TextView tvTempIn;
    TextView tvSelectTemp;
    Button btnCold;
    Button btnDry;
    Button btnTempUp;
    Button btnTempDown;
    Button btnSpeed1;
    Button btnSpeed2;
    Button btnSpeed3;
    Button btnPower;

    public FragmentAirConditioner(SharedObject sharedObject, BufferedReader bufferedReader){
        this.sharedObject=sharedObject;
        this.bufferedReader=bufferedReader;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_airconditioner,container,false);
        context=container.getContext();

        weatherVO = (WeatherVO) getArguments().get("weather");
        Log.v(TAG,"weather.getTemp=="+weatherVO.getTemp());
        sensorDataVO = (SensorDataVO) getArguments().get("sensorData");
        Log.v(TAG,"window.getWindowStatus=="+ sensorDataVO.getWindowStatus());

        tvTempIn = view.findViewById(R.id.tvTempIn);
        tvSelectTemp=view.findViewById(R.id.tvSelectTemp);
        btnCold=view.findViewById(R.id.btnCold);
        btnDry=view.findViewById(R.id.btnDry);
        btnTempUp=view.findViewById(R.id.btnTempUp);
        btnTempDown=view.findViewById(R.id.btnTempDown);
        btnSpeed1=view.findViewById(R.id.btnSpeed1);
        btnSpeed2=view.findViewById(R.id.btnSpeed2);
        btnSpeed3=view.findViewById(R.id.btnSpeed3);
        btnPower=view.findViewById(R.id.btnPower);

        btnCold.setOnClickListener(mClick);
        btnDry.setOnClickListener(mClick);
        btnTempUp.setOnClickListener(mClick);
        btnTempDown.setOnClickListener(mClick);
        btnSpeed1.setOnClickListener(mClick);
        btnSpeed2.setOnClickListener(mClick);
        btnSpeed3.setOnClickListener(mClick);
        btnPower.setOnClickListener(mClick);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        tvTempIn.setText(sensorDataVO.getTemp());
        tvTempIn.setText(weatherVO.getTemp()+"℃");
        tvSelectTemp.setText("20");
    }

    View.OnClickListener mClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnCold:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER COLD");
                    break;
                case R.id.btnDry:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER DRY");
                    break;
                case R.id.btnTempUp:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER UP");
                    tempChange(1);
                    break;
                case R.id.btnTempDown:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER DOWN");
                    tempChange(-1);
                    break;
                case R.id.btnSpeed1:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER SPEED1");
                    break;
                case R.id.btnSpeed2:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER SPEED2");
                    break;
                case R.id.btnSpeed3:
                    sharedObject.put("/ANDROID>/AIRCONDITIONER SPEED3");
                    break;
                case R.id.btnPower:
                    if (sensorDataVO.getAirconditionerStatus().equals("OFF")){
                        sharedObject.put("/ANDROID>/AIRCONDITIONER ON");
                    }else if(sensorDataVO.getAirconditionerStatus().equals("ON")){
                        sharedObject.put("/ANDROID>/AIRCONDITIONER OFF");
                    }
            }
        }
    };

    public void tempChange(int temp){
        int calculation = (Integer.parseInt((String) tvSelectTemp.getText()))+temp;
        tvSelectTemp.setText(String.valueOf(calculation));
//        return  String.valueOf(calculation);
    }
}
