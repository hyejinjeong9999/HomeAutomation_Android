package viewPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.semiproject.LoginActivity;
import com.example.semiproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;

import communication.SharedObject;


public class FragmentSetting extends Fragment {
    String TAG = "FragmentSetting";
    View view;
    Context context;
//    SpeechRecognizer speechRecognizer;
//    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    SharedObject sharedObject;
    BufferedReader bufferedReader;

    ImageView settingProfile;
    TextView settingName;
    TextView settingEmail;
    Button settingLogut;

    public FragmentSetting(SharedObject sharedObject, BufferedReader bufferedReader) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_v, container, false);
        context = container.getContext();

        settingProfile = (ImageView) view.findViewById(R.id.settingProfile);
        settingLogut = (Button) view.findViewById(R.id.settingLogout);
        settingEmail = (TextView) view.findViewById(R.id.settingEmail);

        // google profiles
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);

        settingEmail.setText("유저, '" + acct.getEmail() + "' 님이 입장하셨습니다.");
        Glide.with(context).load(acct.getPhotoUrl()).into(settingProfile);

        // btn_logout
        settingLogut = view.findViewById(R.id.settingLogout);
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

