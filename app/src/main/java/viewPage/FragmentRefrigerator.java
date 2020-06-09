package viewPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;

import communication.SharedObject;


public class FragmentRefrigerator extends Fragment {
    String TAG = "FragmentRefrigerator";
    View view;
    Context context;
    Button btnOnVoice;
    TextView tvSound;
//    SpeechRecognizer speechRecognizer;
//    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentRefrigerator(SharedObject sharedObject, BufferedReader bufferedReader) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_refrigerator,container,false);
//        context=container.getContext();
//        tvSound=view.findViewById(R.id.tvSound);
//        btnOnVoice=view.findViewById(R.id.btnOnVoice);
//
//        btnOnVoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //QR code Scanner Start
//                IntentIntegrator.forSupportFragment(FragmentRefrigerator.this).initiateScan();
//                Log.v(TAG,"onClick()==QR");
//            }
//        });
//
////        if (ContextCompat.checkSelfPermission(this,
////                Manifest.permission.RECORD_AUDIO)
////                != PackageManager.PERMISSION_GRANTED) {
////
////            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
////                    Manifest.permission.RECORD_AUDIO)) {
////
////            } else {
////                ActivityCompat.requestPermissions(this,
////                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
////                );
////            }
////        }
////        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
////        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
////        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
////
////        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
////        speechRecognizer.setRecognitionListener(recognitionListener);
////
////
////        btnOnVoice.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                speechRecognizer.startListening(intent);
////            }
////        });
        return view;
    }

    /**
     * QR code - Zxing Library
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.v(TAG, "onActivityResult" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Log.v(TAG, "result.getContents() == " + scanResult.getContents());
            Toast.makeText(context, scanResult.getContents(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();Name cname = new ComponentName("com.example.booksearchrecyclerviewkakaoapi", "\""+re+"\"");
////            intent.setComponent(cname);
////            startActivity(intent);
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//            Component
        }
    }
}
