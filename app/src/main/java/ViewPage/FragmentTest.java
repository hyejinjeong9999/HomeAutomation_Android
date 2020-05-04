package ViewPage;

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

public class FragmentTest extends Fragment {
    String TAG="FragmentTest";
    View view;
    Context context;
    SeekBar sbLED;
    TextView tvReceiveData;
    Button btnTest;
    Communication.SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentTest(Communication.SharedObject sharedObject, BufferedReader bufferedReader){
        this.sharedObject=sharedObject;
        this.bufferedReader=bufferedReader;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test,container,false);
        context=container.getContext();
        tvReceiveData=view.findViewById(R.id.tvReceiveData);

        btnTest=view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v(TAG,"onClick");
                tvReceiveData.setText("바뀌니???");
            }
        });
        /**
         * asyncTaskTest Object 인자에 bufferedReader 와 TextVIew를 넘겨준다
         */
        Communication.DataReceiveAsyncTaskTest asyncTaskTest =
                new Communication.DataReceiveAsyncTaskTest(bufferedReader, tvReceiveData);
        asyncTaskTest.execute();
        /**
         * SeekBar를 이용해 0-255 까지의 Int값을 받아 sharedObject에 Data를 넘겨준다
         */
        sbLED=view.findViewById(R.id.sbLED);
        sbLED.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v(TAG,"onProgressChanged =="+progress);
                sharedObject.put(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
