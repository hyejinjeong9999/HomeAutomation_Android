package ViewPage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;

import java.io.BufferedReader;

import Communication.SharedObject;


public class FragmentSetting extends Fragment {
    String TAG = "FragmentSetting";
    View view;
    Context context;
//    SpeechRecognizer speechRecognizer;
//    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentSetting(SharedObject sharedObject, BufferedReader bufferedReader) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = container.getContext();

        return view;
    }
}
