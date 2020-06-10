package viewPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.semiproject.LoginActivity;
import com.example.semiproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;

import communication.SharedObject;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FragmentSetting extends Fragment {
    String TAG = "FragmentSetting";
    View view;
    Context context;
//    SpeechRecognizer speechRecognizer;
//    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    SharedObject sharedObject;
    BufferedReader bufferedReader;

    FirebaseUser user;

    ImageView settingProfile;
    TextView settingName;
    TextView settingEmail;
    Button settingLogut;
    Switch settingVoiceRecognitionBtn;
    boolean voiceRecognition;
    private SharedPreferences appData;

    public FragmentSetting(SharedObject sharedObject, BufferedReader bufferedReader) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_v, container, false);
        context = container.getContext();

        appData = context.getSharedPreferences("appData", context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = appData.edit();

        voiceRecognition = appData.getBoolean("VOICE_RECOGNITION", false);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);

        settingProfile = view.findViewById(R.id.settingProfile);
        settingName = view.findViewById(R.id.settingName);
        settingEmail = view.findViewById(R.id.settingEmail);
        settingLogut = view.findViewById(R.id.settingLogout);
        settingVoiceRecognitionBtn = view.findViewById(R.id.settingVoiceRecognitionBtn);


        String str = "test";


        //profiles
        Glide.with(context).load(acct.getPhotoUrl()).into(settingProfile);
        settingProfile.setBackground(new ShapeDrawable(new OvalShape()));
        settingProfile.setClipToOutline(true);
        settingName.setText(acct.getDisplayName());
        settingEmail.setText("유저, '" + acct.getEmail() + "' 님이 입장하셨습니다.");

        settingVoiceRecognitionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isChecked){
                            editor.putBoolean("VOICE_RECOGNITION", isChecked);
                            editor.apply();
                        }else{
                            editor.putBoolean("VOICE_RECOGNITION", isChecked);
                            editor.apply();
                        }
                    }
                });
            }
        });
        if(voiceRecognition){
            settingVoiceRecognitionBtn.setChecked(true);
        }else{
            settingVoiceRecognitionBtn.setChecked(false);
        }


        // btn_logout
        settingLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(context, LoginActivity.class);
                startActivity(intToMain);
            }
        });

        return view;
    }

}

