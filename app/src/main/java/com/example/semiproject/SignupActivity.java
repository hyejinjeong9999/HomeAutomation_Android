package com.example.semiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SignupActivity extends AppCompatActivity{
    String TAG = "SignupActivity";
    public EditText emailId, password;
    String email, pwd, google_profile, google_email = "";
    TextView loginTv;
    Button signUpBtn;
    // FirebaseAuth
    private FirebaseAuth mAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        signUpBtn = (Button)findViewById(R.id.btn_signup);
        loginTv = findViewById(R.id.textView);

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
                Toast.makeText(SignupActivity.this, "페이스북 로그인 성공", Toast.LENGTH_LONG).show();
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
                mAuth.startActivityForSignInWithProvider(SignupActivity.this, provider.build())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
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

        // custom email signup
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    email = emailId.getText().toString();
                    pwd = password.getText().toString();
                }catch(Exception e){
                }

                if(email.isEmpty()){
                    emailId.setError("이메일을 확인하세요");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("비밀번호를 확인하세요");
                    password.requestFocus();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignupActivity.this, "로그인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intLogin = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });

        // Google Login
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
    } // onCreate end

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, req_code);
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
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "구글 인증 완료", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser gFirebaseUser = mAuth.getCurrentUser();
                            google_profile = String.valueOf(acct.getPhotoUrl());
                            google_email = acct.getEmail();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Snackbar.make(findViewById(R.id.sample_snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(SignupActivity.this, "구글 인증에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                            Toast.makeText(SignupActivity.this, "fb_email: " + fb_email + " : Login~", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

