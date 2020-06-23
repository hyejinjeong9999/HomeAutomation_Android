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
    private CallbackManager mCallbackManager;     // facebook Login
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");   // github
    // remember ID
    boolean isRemembered = false;
    private SharedPreferences appData;
    public static Activity loginActivity;

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
        mCallbackManager = CallbackManager.Factory.create(); // facebook 로그인 응답을 처리할 콜백 관리자

        // remember ID; 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        if (isRemembered) {
            emailId.setText(email);
            chbx_remember.setChecked(isRemembered);
        }

        // LoginListener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "로그인 중입니다..", Toast.LENGTH_SHORT).show();
                } else {
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
                loginButton.performClick();
            }
        });

        // Register a callback to respond to the user
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "페이스북 로그인 성공", Toast.LENGTH_LONG).show();
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
        }); // facebook login end

        // github login
        iv_ic_github = (ImageView) findViewById(R.id.iv_ic_github);
        iv_ic_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.startActivityForSignInWithProvider(LoginActivity.this, provider.build())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
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
                                // Handle failure.
                            }
                        });

            }
        }); // github login end

        // custom email login
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    email = emailId.getText().toString();
                    pwd = password.getText().toString();
                } catch (Exception e) {
                }

                if (email.isEmpty()) {
                    emailId.setError("Please Check Email");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please Check your password");
                    password.requestFocus();
                } else {
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "아이디를 저장합니다", Toast.LENGTH_SHORT).show();
                                startActivity( new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "로그인 중입니다", Toast.LENGTH_SHORT).show();
                                finish();
                                save();

                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 에러", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }); // custom login end

        // if no account -> SignUp first
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
        }); // Google Login end

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }// onCreate end

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
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Snackbar.make(findViewById(R.id.sample_snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "구글 인증에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
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
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }
            // The Task returned from this call is always completed, no need to attach
            // a listener.
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String fb_profile = String.valueOf(user.getPhotoUrl());
                            String fb_email = user.getEmail();
                            Toast.makeText(LoginActivity.this, "fb_email: " + fb_email + " : Login~", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // remember ID; save(), load();
    // 설정 값 저장하기
    private void save() {
        // 객체만 저장 불가능, Editor, edit() 사용;
        SharedPreferences.Editor editor = appData.edit();

        editor.putString("ID", emailId.getText().toString().trim());
        editor.putBoolean("SAVE_LOGIN_DATA", chbx_remember.isChecked());

        // apply.commit 을 해야 변경된 내용 저장
        editor.apply();
    }
    // 설정 값 불러오기
    private void load() {
        isRemembered = appData.getBoolean("SAVE_LOGIN_DATA", false);
        email = appData.getString("ID", "");
    }
}