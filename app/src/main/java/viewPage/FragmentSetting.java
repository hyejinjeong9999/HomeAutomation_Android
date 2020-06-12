package viewPage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.semiproject.MainActivity;
import com.example.semiproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    // firebaseAuth
    String userEmail, userName;
    Uri userPhotoURI;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;

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
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        // custom firebaseAuth profiles
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (acct != null) {     //  google acct profiles
            Log.i("ltest", "acct != null");
            settingEmail.setText("유저, '" + acct.getEmail() + "' 님이 입장하셨습니다.");
            Glide.with(context).load(acct.getPhotoUrl()).into(settingProfile);
        }else if(user != null){ //  custom firebaseAuth profiles
            Log.i("ltest", "user != null");
            // Name, email address, and profile photo Url
            userName = user.getDisplayName();
            userEmail = user.getEmail();
            userPhotoURI = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            Log.i("ltest", userEmail + " / " +userName + " / " +  userPhotoURI);

            settingEmail.setText("유저, '" + userEmail + "' 님이 입장하셨습니다. " +
                    "\n" + " 반갑습니당, '" + userName + "'님");
            Glide.with(context).load(userPhotoURI).into(settingProfile);
        }


//        if (acct != null) {
//            settingEmail.setText("유저, '" + acct.getEmail() + "' 님이 입장하셨습니다.");
//            Glide.with(context).load(acct.getPhotoUrl()).into(settingProfile);
//        }else{
//            settingEmail.setText("Null");
//        }
//
//        // firebaseAuth profiles
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            userName = user.getDisplayName();
//            userEmail = user.getEmail();
//            userPhotoURI = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            settingEmail.setText("유저, '" + userEmail + "' 님이 입장하셨습니다. " +
//                    "\n" + " 반갑습니당, '" + userName + "'님");
//            Glide.with(context).load(userPhotoURI).into(settingProfile);
//        } else {
//            settingEmail.setText("NotloggedIn");
//            Toast.makeText(context, "user: null이 떠버렸는데요?", Toast.LENGTH_SHORT).show();
//        }

//        userEmail = mFirebaseAuth.getCurrentUser().getEmail();
//        Log.i("ltest", "email: " + userEmail);
//        userName = mFirebaseAuth.getCurrentUser().getDisplayName();
//        Log.i("ltest", "name: " + userName);
//        userPhotoURI = mFirebaseAuth.getCurrentUser().getPhotoUrl();
//        Log.i("ltest", "photo: " + String.valueOf(userPhotoURI));
//        settingEmail.setText("유저, '" + userEmail + "' 님이 입장하셨습니다.");
//        Glide.with(context).load(userPhotoURI).into(settingProfile);

        // btn_logout
        settingLogut = view.findViewById(R.id.settingLogout);
        settingLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firebase user logout
                Log.i("ltest", "logout~");
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
                googleSignInClient.signOut();

                ((MainActivity)getActivity()).finish();
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}

