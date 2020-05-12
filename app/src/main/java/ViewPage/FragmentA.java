package ViewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;

import java.io.BufferedReader;

import Communication.SharedObject;
import model.WeatherVO;

public class FragmentA extends Fragment {
    private String TAG="FragmentA";
    private View view;
    private SharedObject sharedObject;
    private BufferedReader bufferedReader;
    private Context context;
    private TextView fragATV01;
    private ToggleButton toggleBtn;
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

        // 창문 버튼 누르기
        toggleBtn = view.findViewById(R.id.fragAToggleBtn);
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleBtn.isChecked()){
                   Toast.makeText(context, "CLOSED", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "OPEN", Toast.LENGTH_SHORT).show();
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
