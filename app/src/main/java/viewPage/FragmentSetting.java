package viewPage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.semiproject.LoginActivity;
import com.example.semiproject.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Objects;

import communication.SharedObject;
import model.AirconditionerVO;
import model.LogVO;
import recyclerViewAdapter.LogAdapter;


public class FragmentSetting extends Fragment {
    String TAG = "FragmentSetting";
    View view;
    Context context;
//    SpeechRecognizer speechRecognizer;
//    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    SharedObject sharedObject;
    BufferedReader bufferedReader;
    LogVO logVO;
    LogAdapter logAdapter;
    ArrayList<AirconditionerVO> airconditionerData  = new ArrayList<>();
    ArrayList<String> airconditionerData1 = new ArrayList<>();
    String[] abcData = new String[10];
    String logDataStatus;

    ImageView settingProfile;
    TextView settingName;
    TextView settingEmail;
    Button settingLogut;
    Button btnairconditioner;
    Button btnAirpurifier;
    Button btnWindow;
    Switch settingVoiceRecognitionBtn;
    ListView lvLog;

    boolean voiceRecognition;
    private SharedPreferences appData;

    // firebaseAuth
    String userEmail, userName;
    Uri userPhotoURI;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    GoogleSignInAccount acct;

    public FragmentSetting(SharedObject sharedObject, BufferedReader bufferedReader, FirebaseUser user) {
        this.sharedObject = sharedObject;
        this.bufferedReader = bufferedReader;
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_v, container, false);
        context = container.getContext();

        appData = context.getSharedPreferences("appData", context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = appData.edit();

        voiceRecognition = appData.getBoolean("VOICE_RECOGNITION", false);

        // firebase
        acct = GoogleSignIn.getLastSignedInAccount(context);

        settingProfile = view.findViewById(R.id.settingProfile);
//        settingName = view.findViewById(R.id.settingName);
        settingEmail = view.findViewById(R.id.settingEmail);
        settingLogut = view.findViewById(R.id.settingLogout);
        btnairconditioner = view.findViewById(R.id.btnairconditioner);
        btnAirpurifier = view.findViewById(R.id.btnAirpurifier);
        btnWindow = view.findViewById(R.id.btnWindow);
        settingVoiceRecognitionBtn = view.findViewById(R.id.settingVoiceRecognitionBtn);

        btnairconditioner.setOnClickListener(mClick);
        btnAirpurifier.setOnClickListener(mClick);
        btnWindow.setOnClickListener(mClick);
        settingLogut.setOnClickListener(mClick);

//        lvLog = view.findViewById(R.id.lvLog);
        logVO = (LogVO)getArguments().get("LOGVO");


        //profiles

        //Glide.with(context).load(acct.getPhotoUrl()).into(settingProfile);
        settingProfile.setBackground(new ShapeDrawable(new OvalShape()));
        settingProfile.setClipToOutline(true);
        //settingName.setText(acct.getDisplayName());
        //settingEmail.setText("유저, '" + acct.getEmail() + "' 님이 입장하셨습니다.");

        // custom firebaseAuth profiles
        user = mFirebaseAuth.getInstance().getCurrentUser();

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

            Log.i("ltest", userEmail + " / " +userName + " / " +  userPhotoURI);

            settingEmail.setText("유저, '" + userEmail + "' 님이 입장하셨습니다. " +
                    "\n" + " 반갑습니당, '" + userName + "'님");
            Glide.with(context).load(userPhotoURI).into(settingProfile);
        }
        /*else {
            Log.i("ltest", "else !");
            // Name, email address, and profile photo Url
            for (UserInfo userInfo : user.getProviderData()) {
                if (userName == null && userInfo.getDisplayName() != null) {
                    userName = userInfo.getDisplayName();
                }
                if (userEmail == null && userInfo.getEmail() != null) {
                    userEmail = userInfo.getEmail();
                }
            }
            userEmail = user.getEmail();
            userName = user.getDisplayName();
            Log.i("ltest", userEmail + " / " +userName + " / " +  userPhotoURI);
            settingName.setText(userName);
            settingEmail.setText("유저, '" + userEmail + "' 님이 입장하셨습니다. " +
                    "\n" + " 반갑습니당, '" + userName + "'님");
        }*/


        settingVoiceRecognitionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isChecked) {
                            editor.putBoolean("VOICE_RECOGNITION", true);
                            editor.apply();
                        } else {
                            editor.putBoolean("VOICE_RECOGNITION", false);
                            editor.apply();
                        }
                    }
                });
            }
        });
        if (voiceRecognition) {
            settingVoiceRecognitionBtn.setChecked(true);
        } else {
            settingVoiceRecognitionBtn.setChecked(false);
        }
        // btn
//        btnLog.setOnClickListener(mClick);
//        settingLogut.setOnClickListener(mClick);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        voiceRecognition = appData.getBoolean("VOICE_RECOGNITION", false);
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.settingLogout:
                    if(acct != null){
                    }else if(user != null){

                    }
                    alertsignout();
                    break;
                case R.id.btnairconditioner:
                    recyclerViewCall("Airconditioner");
                    break;
                case R.id.btnAirpurifier:
                    recyclerViewCall("Airpurifier");
                    break;
                case R.id.btnWindow:
                    recyclerViewCall("Window");
                    break;
            }
        }
    };

    public void alertsignout() {
        AlertDialog.Builder signOutAlertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        signOutAlertDialog.setTitle("확인");

        // Setting Dialog Message
        signOutAlertDialog.setMessage("로그아웃 하시겠습니까?");

        // Setting Positive "Yes" Btn
        signOutAlertDialog.setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(acct != null){
                            // google signout
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
                            googleSignInClient.signOut();
                            Objects.requireNonNull(getActivity()).finish();
                        }else{
                            LoginManager.getInstance().logOut();
                            FirebaseAuth.getInstance().signOut();
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        Intent i = new Intent(context, LoginActivity.class);
                        /* TODO: 언제 finish()룰 해야하는걸까? 알아보기*/
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        /* TODO: 언제 finish()룰 해야하는걸까? 알아보기*/


        // Setting Negative "NO" Btn
        signOutAlertDialog.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"로그아웃 취소", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        signOutAlertDialog.show();
    }

    public void recyclerViewCall(String keyword){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
        logAdapter = new LogAdapter(context,sharedObject,logVO,keyword);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(logAdapter);
    }
}

