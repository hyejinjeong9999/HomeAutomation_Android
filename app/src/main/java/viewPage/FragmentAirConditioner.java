package viewPage;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    String TAG = "FragmentAirConditioner";
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

    private SharedPreferences appData;
    SharedPreferences.Editor editor;
    int savedTemp;

    public FragmentAirConditioner(SharedObject sharedObject, BufferedReader bufferedReader, SensorDataVO sensorDataVO) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
        this.sensorDataVO = sensorDataVO;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_airconditioner_vvvvvvvvvv, container, false);
        context = container.getContext();

        appData = context.getSharedPreferences("appData", context.MODE_PRIVATE);
        editor = appData.edit();


        weatherVO = (WeatherVO) getArguments().get("weather");
        Log.v(TAG, "weather.getTemp==" + weatherVO.getTemp());
        sensorDataVO = (SensorDataVO) getArguments().get("sensorData");
        Log.v(TAG, "window.getWindowStatus==" + sensorDataVO.getWindowStatus());

        tvTempIn = view.findViewById(R.id.tvTempIn);
        tvSelectTemp = view.findViewById(R.id.tvSelectTemp);
        btnCold = view.findViewById(R.id.btnCold);
        btnDry = view.findViewById(R.id.btnDry);
        btnTempUp = view.findViewById(R.id.btnTempUp);
        btnTempDown = view.findViewById(R.id.btnTempDown);
        btnSpeed1 = view.findViewById(R.id.btnSpeed1);
        btnSpeed2 = view.findViewById(R.id.btnSpeed2);
        btnSpeed3 = view.findViewById(R.id.btnSpeed3);
        btnPower = view.findViewById(R.id.btnPower);

        btnCold.setOnClickListener(mClick);
        btnDry.setOnClickListener(mClick);
        btnTempUp.setOnClickListener(mClick);
        btnTempDown.setOnClickListener(mClick);
        btnSpeed1.setOnClickListener(mClick);
        btnSpeed2.setOnClickListener(mClick);
        btnSpeed3.setOnClickListener(mClick);
        btnPower.setOnClickListener(mClick);

        if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
            tvSelectTemp.setText(sensorDataVO.getAirconditionerTemp());
            setCliked(btnPower, context);
            if (sensorDataVO.getAirconditionerMode().equals("COLD")) {
                setCliked(btnCold, context);
                unCliked(btnDry, context);
            } else if (sensorDataVO.getAirconditionerMode().equals("DRY")) {
                setCliked(btnDry, context);
                unCliked(btnCold, context);
            }
            if (sensorDataVO.getAirconditionerSpeed().equals("SPEED1")) {
                setCliked(btnSpeed1, context);
                unCliked(btnSpeed2, context);
                unCliked(btnSpeed3, context);
            } else if (sensorDataVO.getAirconditionerSpeed().equals("SPEED2")) {
                unCliked(btnSpeed1, context);
                setCliked(btnSpeed2, context);
                unCliked(btnSpeed3, context);
            } else if (sensorDataVO.getAirconditionerSpeed().equals("SPEED3")) {
                unCliked(btnSpeed1, context);
                unCliked(btnSpeed2, context);
                setCliked(btnSpeed3, context);
            }
        } else {
            unCliked(btnPower, context);
            unCliked(btnDry, context);
            unCliked(btnCold, context);
            unCliked(btnSpeed1, context);
            unCliked(btnSpeed2, context);
            unCliked(btnSpeed3, context);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        tvTempIn.setText(sensorDataVO.getTemp());
        //tvTempIn.setText(sensorDataVO.getTemp() + "℃");
        //tvSelectTemp.setText("26");
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCold:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        sharedObject.put("/ANDROID>/AIRCONDITIONER COLD");
                        setCliked(btnCold, context);
                        unCliked(btnDry, context);
                    }
                    break;
                case R.id.btnDry:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        sharedObject.put("/ANDROID>/AIRCONDITIONER dry");
                        unCliked(btnCold, context);
                        setCliked(btnDry, context);
                    }
                    break;
                case R.id.btnTempUp:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        if (Integer.parseInt(tvSelectTemp.getText().toString()) < 30) {
                            sharedObject.put("/ANDROID>/AIRCONDITIONER " + tempChange(1));
                            editor.putInt("AIRCONDITIONOR_TEMP", Integer.parseInt((String) tvSelectTemp.getText()));
                            editor.apply();
                        }
                    }
                    break;
                case R.id.btnTempDown:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        if (Integer.parseInt(tvSelectTemp.getText().toString()) > 18) {
                            sharedObject.put("/ANDROID>/AIRCONDITIONER " + tempChange(-1));
                            editor.putInt("AIRCONDITIONOR_TEMP", Integer.parseInt((String) tvSelectTemp.getText()));
                            editor.apply();
                        }
                    }
                    break;
                case R.id.btnSpeed1:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        sharedObject.put("/ANDROID>/AIRCONDITIONER speed1");
                        setCliked(btnSpeed1, context);
                        unCliked(btnSpeed2, context);
                        unCliked(btnSpeed3, context);
                    }
                    break;
                case R.id.btnSpeed2:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        sharedObject.put("/ANDROID>/AIRCONDITIONER speed2");

                        unCliked(btnSpeed1, context);
                        setCliked(btnSpeed2, context);
                        unCliked(btnSpeed3, context);
                    }
                    break;
                case R.id.btnSpeed3:
                    if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        sharedObject.put("/ANDROID>/AIRCONDITIONER speed3");

                        unCliked(btnSpeed1, context);
                        unCliked(btnSpeed2, context);
                        setCliked(btnSpeed3, context);
                    }
                    break;
                case R.id.btnPower:
                    Log.v(TAG, "sensorDataVO.getAirconditionerStatus()==" + sensorDataVO.getAirconditionerStatus());
                    if (sensorDataVO.getAirconditionerStatus().equals("OFF")) {
                        Log.v(TAG, sensorDataVO.getAirconditionerStatus());
                        sharedObject.put("/ANDROID>/AIRCONDITIONER ON");
                        savedTemp = appData.getInt("AIRCONDITIONOR_TEMP", 26);
                        Log.v(TAG, "저장된 값은 ======" + savedTemp);
                        setCliked(btnPower, context);
                        setCliked(btnCold, context);
                        unCliked(btnDry, context);
                        tvSelectTemp.setText(String.valueOf(savedTemp));
                        setCliked(btnSpeed1, context);
                        unCliked(btnSpeed2, context);
                        unCliked(btnSpeed3, context);
                        sharedObject.put("/ANDROID>/AIRCONDITIONER " +savedTemp);
                    } else if (sensorDataVO.getAirconditionerStatus().equals("ON")) {
                        Log.v(TAG, sensorDataVO.getAirconditionerStatus());
                        sharedObject.put("/ANDROID>/AIRCONDITIONER OFF");
                        unCliked(btnPower, context);
                        unCliked(btnCold, context);
                        unCliked(btnDry, context);
                        unCliked(btnSpeed1, context);
                        unCliked(btnSpeed2, context);
                        unCliked(btnSpeed3, context);
                        editor.putInt("AIRCONDITIONOR_TEMP", Integer.parseInt((String) tvSelectTemp.getText()));
                        editor.apply();
                    }
            }
        }
    };

    public String tempChange(int temp) {
        int calculation = (Integer.parseInt((String) tvSelectTemp.getText())) + temp;
        tvSelectTemp.setText(String.valueOf(calculation));
        return String.valueOf(calculation);
    }

    public void setCliked(Button button, Context context) {
        button.setBackgroundResource(R.drawable.air_temp_line_clicked);
        button.setTextColor(context.getResources().getColor(R.color.fontDark, null));
    }

    public void unCliked(Button button, Context context) {
        button.setBackgroundResource(R.drawable.air_temp_line);
        button.setTextColor(context.getResources().getColor(R.color.recyclerViewItemFont, null));
    }
}
