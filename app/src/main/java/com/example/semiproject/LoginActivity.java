package com.example.semiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    public EditText emailId, password;
    String email, pwd, google_profile, google_email = "";
    CheckBox chbx_remember;

    Button signInBtn;
    TextView signUpTv;
    //  FirebaseAuth
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // Access a Cloud Firestore instance from your Activity
    // FirebaseFirestore db = FirebaseFirestore.getInstance();
    // google Login
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView iv_ic_google;
    private ImageView iv_ic_facebook;
    private LoginButton loginButton;
    private static final int req_code = 4122;
    // facebook Login
    private CallbackManager mCallbackManager;
    // kakao login
    String uid = "some-uid";
    // remember ID
    boolean isRemembered = false;
    private SharedPreferences appData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //facebook activate logging
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        emailId = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        chbx_remember = (CheckBox) findViewById(R.id.chbx_remember);
        signInBtn = (Button) findViewById(R.id.btn2);
        signUpTv = (TextView) findViewById(R.id.textView);


        // remember ID; 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(isRemembered){
            emailId.setText(email);
            chbx_remember.setChecked(isRemembered);
        }
/*

        // 세션 콜백 구현
        private ISessionCallback sessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                Log.i("KAKAO_SESSION", "로그인 성공");
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Log.e("KAKAO_SESSION", "로그인 실패", exception);
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // 세션 콜백 등록
            Session.getCurrentSession().addCallback(sessionCallback);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            // 세션 콜백 삭제
            Session.getCurrentSession().removeCallback(sessionCallback);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return;
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
*/

        // google Login
        iv_ic_google = findViewById(R.id.iv_ic_google);
// 여기서 충돌??
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "You are logged in..", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Please LogIn", Toast.LENGTH_SHORT).show();

                }
            }
        };


        // facebook login
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPermissions("email");
            // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        // facebook loginManager callback
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        // custom email login
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    email = emailId.getText().toString();
                    pwd = password.getText().toString();
                }catch (Exception e){
                    Log.i("test", e.toString());
                }

                if(email.isEmpty()){
                    emailId.setError("Please Check Email");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please Check your password");
                    password.requestFocus();
                }else{
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, check again", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.i("ltest", "isSuccessful 01");
                                Intent intHome = new Intent(LoginActivity.this, MainActivity.class);
                                Log.i("ltest", "isSuccessful 02");
                                startActivity(intHome);
                                Log.i("ltest", "isSuccessful 03");
                                save();
                                Log.i("ltest", "save()");
                            }
                        }
                    });
                }
            }
        });

        // no account -> SignUp first
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intSignUp);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthStateListener);


        // Google Login start
        iv_ic_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_ic_google:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStop() {
        Log.i("LoginTest", "onStop");
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
            FirebaseAuth.getInstance().signOut();
            Log.i("LoginTest", "onStop;signOut()");
        }
    }

    private void signIn() {
        Log.i("LoginTest", "signIn");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.i("LoginTest", "signIn;Intent signInIntent");
        startActivityForResult(signInIntent, req_code);
        Log.i("LoginTest", "signIn;req_code");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == req_code) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                Log.i("LoginTest", " GoogleSignInAccount account = result.getSignInAccount();");
                firebaseAuthWithGoogle(account);
                Log.i("LoginTest", "firebaseAuthWithGoogle(account);");
            }else{

            }
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            // ??
            /*Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
        }

        // facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //updateUI(null);
            Log.i("test", e.toString());
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("DEBUG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            Log.i("ltest", "Authentication Success");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            save(); // remember
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
/*

    // kakao Login start
    private void firebaseAuthWithKakao(KakaoSdk token){

        AuthCredential credential= KakaoAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
*/



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            google_profile = String.valueOf(acct.getPhotoUrl());
                            google_email = acct.getEmail();
                            Log.i("ltest", "email > " + acct.getEmail());
                            Log.i("ltest", "uri > " + acct.getPhotoUrl());
                            Log.i("ltest", "login_success_firebaseAuthWithGoogle");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            // Snackbar.make(findViewById(R.id.sample_snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }   // google Login End

// remember ID; save(), load();
    // 설정 값 저장하기
    private void save(){
        // 객체만 저장 불가능, Editor, edit() 사용;
        SharedPreferences.Editor editor = appData.edit();

        Log.i("ltest", "save().email: " + editor.putString("ID", emailId.getText().toString().trim()));
        editor.putString("ID", emailId.getText().toString().trim());
        Log.i("ltest", "save().checked: " + editor.putBoolean("SAVE_LOGIN_DATA", chbx_remember.isChecked()));
        editor.putBoolean("SAVE_LOGIN_DATA", chbx_remember.isChecked());

        // apply.commit 을 해야 변경된 내용 저장
        editor.apply();
    }

    // 설정 값 불러오기
    private void load(){
        isRemembered = appData.getBoolean("SAVE_LOGIN_DATA", false);
        Log.i("ltest", "load()");
        email = appData.getString("ID", "");
        Log.i("ltest", "load().email: " + email);
    }
}
