package com.example.semiproject;

import android.app.Activity;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    public EditText emailId, password;
    String email, pwd, google_profile, google_email = "";
    CheckBox chbx_remember;

    Button signInBtn;
    TextView signUpTv;
    //  FirebaseAuth
    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // google Login
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView iv_ic_google;
    private ImageView iv_ic_facebook;
    private ImageView iv_ic_github;
    private LoginButton loginButton;
    private static final int req_code = 4122;
    // facebook Login
    private CallbackManager mCallbackManager;
    // kakao login
    String uid = "some-uid";
    // remember ID
    boolean isRemembered = false;
    private SharedPreferences appData;
    public static Activity loginActivity;
    // github
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");


    /* TODO:언젠가 하겠지 카카오 로그인...시간과 예산의 부족*/
    /*
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name, token = "";
    private SessionCallback callback;      //콜백 선언
    final static String TAG = "LoginActivityT";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //facebook activate logging
        AppEventsLogger.activateApp(getApplication());

        loginActivity = LoginActivity.this;

        mAuth = FirebaseAuth.getInstance();
        emailId = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        chbx_remember = (CheckBox) findViewById(R.id.chbx_remember);
        signInBtn = (Button) findViewById(R.id.btn2);
        signUpTv = (TextView) findViewById(R.id.textView);
        // facebook 로그인 응답을 처리할 콜백 관리자
        mCallbackManager = CallbackManager.Factory.create();
        // remember ID; 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(isRemembered){
            emailId.setText(email);
            chbx_remember.setChecked(isRemembered);
        }

        /* TODO:언젠가 하겠지 카카오 로그인...시간과 예산의 부족*/
        /*

        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
//        //키값 알아내기(알아냈으면 등록하고 지워도 상관없다)
//        getAppKeyHash();

        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
        loadShared();



    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash: >>", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
*/

        // LoginListener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                mFirebaseUser = mAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "로그인 중입니다..", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(i);
                }else{
                    Toast.makeText(LoginActivity.this, "로그인 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        };
        // facebook 유효성 검사
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        // facebook login
        iv_ic_facebook = (ImageView) findViewById(R.id.iv_ic_facebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList("EMAIL", "USER_POSTS"));

        iv_ic_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                loginButton.performClick();
                /*
                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("public_profile", "email"));
//                Arrays.asList("public_profile", "user_friends"));


                */
            }
        });

        // Register a callback to respond to the user
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "페이스북 로그인 성공", Toast.LENGTH_LONG).show();
                Log.i("test", "일단 facebook loginbutton callback ");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
/*
        // Callback registration
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "loginManager:Login 성공", Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.i("test", "일단 facebook callback ");

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login 안해", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        */
        // github login
        iv_ic_github = (ImageView) findViewById(R.id.iv_ic_github);
        iv_ic_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.startActivityForSignInWithProvider(LoginActivity.this, provider.build())
                        .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.i("test", "startActivityForSignInWithProvider # User is signed in \n"
                                 + authResult.toString());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("test", "startActivityForSignInWithProvider # github 로그인 실패 \n"
                                                + e.toString());
                                        // Handle failure.
                                    }
                                });

            }
        });
/*
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                                    Log.i("test", "pendingResultTask # User is signed in");
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                                    Log.i("test", "pendingResultTask # github 로그인 실패");
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
        }*/
//        // The user is already signed-in.
//        mFirebaseUser = mAuth.getCurrentUser();
//        mFirebaseUser.startActivityForLinkWithProvider(/* activity= */ this, provider.build())
//                .addOnSuccessListener(
//                        new OnSuccessListener<AuthResult>() {
//                            @Override
//                            public void onSuccess(AuthResult authResult) {
//                                // GitHub credential is linked to the current user.
//                                // IdP data available in
//                                // authResult.getAdditionalUserInfo().getProfile().
//                                // The OAuth access token can also be retrieved:
//                                // authResult.getCredential().getAccessToken().
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle failure.
//                            }
//                        });

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
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "아이디를 저장합니다", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                Log.i("test", "custom 로그인 간다~");
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "로그인 중입니다", Toast.LENGTH_SHORT).show();
                                finish();
                                save();

                            }else {
                                Toast.makeText(LoginActivity.this, "로그인 에러", Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });

        // google Login
        iv_ic_google = findViewById(R.id.iv_ic_google);
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

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "구글 인증 완료", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser gFirebaseUser = mAuth.getCurrentUser();
                            google_profile = String.valueOf(acct.getPhotoUrl());
                            google_email = acct.getEmail();
                            Log.i("test", "google 로그인 간다~");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Snackbar.make(findViewById(R.id.sample_snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "구글 인증에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }   // google Login End

    @Override
    public void onStart() {
        Log.i("LoginTest", "onStart");
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        Log.i("LoginTest", "onStop");
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, req_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // google
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == req_code) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{}
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            // ??
            /*Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
        }
/*        // kakao
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }*/
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.i("test", "AccessToken token ~" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.i("test", "AccessToken token ~" + token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("test", "isSuccessful");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String fb_profile = String.valueOf(user.getPhotoUrl());
                            String fb_email = user.getEmail();
                            Toast.makeText(LoginActivity.this, "fb_email: " + fb_email + " : Login~", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            Log.i("test", "facebook 로그인 간다~");
                            startActivity(i);
                            finish();
                        } else {
                            Log.i("test", "!isSuccessful ~");
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

/*
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
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // remember ID; save(), load();
    // 설정 값 저장하기
    private void save(){
        // 객체만 저장 불가능, Editor, edit() 사용;
        SharedPreferences.Editor editor = appData.edit();

        editor.putString("ID", emailId.getText().toString().trim());
        editor.putBoolean("SAVE_LOGIN_DATA", chbx_remember.isChecked());

        // apply.commit 을 해야 변경된 내용 저장
        editor.apply();
    }

    // 설정 값 불러오기
    private void load(){
        isRemembered = appData.getBoolean("SAVE_LOGIN_DATA", false);
        email = appData.getString("ID", "");
    }
/* TODO:언젠가 하겠지 카카오 로그인...시간과 예산의 부족*/
/*

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.i("kakao", "세션 오픈");
            requestMe();
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i("kakao", "로그인 망함");
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "onFailure by kakao", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(LoginActivity.this, "onSessionClosed by kakao", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNotSignedUp() {
                Toast.makeText(LoginActivity.this, "onNotSignedUp by kakao", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함
            @Override
            public void onSuccess(final UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Toast.makeText(LoginActivity.this, "onSuccess by kakao", Toast.LENGTH_SHORT).show();

                Logger.d("UserProfile : " + userProfile);
                Log.d(TAG, "유저가입성공");
                // Create a new user with a first and last name
                // 유저 카카오톡 아이디 디비에 넣음(첫가입인 경우에만 디비에저장)

                Map<String, String> user = new HashMap<>();
                user.put("token", userProfile.getId() + "");
                user.put("name", userProfile.getNickname());
                db.collection("users")
                        .document(userProfile.getId() + "")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "유저정보 디비삽입 성공");
                                saveShared(userProfile.getId() + "", userProfile.getNickname());
                            }
                        });
                // 로그인 성공시 MainActivity로
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //입력값 저장
    private void saveShared( String id, String name) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", id);
        editor.putString("name", name);
        editor.apply();
    }

    //불러오기
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getString("token", "");
        name = pref.getString("name", "");
    }


    // kakao firebase Login start
    private void firebaseAuthWithKakao(KakaoSdk token){

        AuthCredential credential= KakaoAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            mFirebaseUser = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    */
}