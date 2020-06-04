package viewPage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;

import java.io.BufferedReader;

import communication.SharedObject;

public class FragmentLight extends Fragment {
    String TAG="FragmentLight";
    View view;
    Context context;
    SharedObject sharedObject;
    BufferedReader bufferedReader;

    TextView tvVoice;

    public FragmentLight(SharedObject sharedObject, BufferedReader bufferedReader){
        this.sharedObject=sharedObject;
        this.bufferedReader=bufferedReader;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light,container,false);
        context=container.getContext();
        Bundle bundle = getArguments();
        tvVoice = view.findViewById(R.id.tvVoice);
//        if(bundle.getString("voice") != null){
//            String voice = (String) bundle.getString("voice");
//            Log.v(TAG,"voice=="+voice);
//            tvVoice.setText(voice);
//        }

        return view;
    }
}
