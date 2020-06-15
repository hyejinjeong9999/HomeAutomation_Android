package com.example.semiproject;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 *  Output before app start
 */
public class SplashActivity extends AppCompatActivity {
    String TAG="SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //보안 설정
        //1. 보안처리 (AndroidManifest.xml파일에 기본 보안에 대한 설정)
        //1.1 Android version 이 마쉬멜로우 버전 이전, 이후인지 check
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //1.1.1 Version M 이상
            Log.v(TAG,"Version Check=="+Build.VERSION.SDK_INT);
            int permissionResult = ActivityCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.RECORD_AUDIO);

            if(permissionResult == PackageManager.PERMISSION_DENIED){
                //권한 없을 경우
                //1. 앱을 처음 실행해서 물어본적이 없는경우
                //2. 권한 허용에 대해서 사용자에게 물어보았지만 사용자가 거절을 선택 했을경우
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)){
                    //true => 권한을 거부한적 있는 경우(일반적으로 dialog를 이용해서 다시 물어봄)
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                    dialog.setTitle("권한 필요");
                    dialog.setMessage("다음 항목 권한이 필요합니다, 수락할거니");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v(TAG,"dialog_onClick==YES");
                            requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},100);
                        }
                    });
                    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v(TAG,"dialog_onClick==NO");
                        }
                    });
                    dialog.create().show();
                }else {
                    //false => 한번도 물어본적 없는경우
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,android.Manifest.permission.CAMERA},100);
                    //여러개의 권한을 물어볼수 있기 때문에 String[] 배열에 넣어줌 한뻐너에 다 처리할 수 있음
                }
            }else {
                //권한 있을 경우
                Log.v(TAG,"Check=="+permissionResult+" /보안설정 통과");

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        }else {
            //1.1.2 Version M 미만
            Log.v(TAG,"Version Check=="+Build.VERSION.SDK_INT+" /보안설정 통과");
            //startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));

            finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG,"onRequestPermissionsResult() _ requestCode=="+requestCode);
        //사용자가 권한을 설정하게 되면 이 Method가 마지막으로 호출 됨.
        if(requestCode == 100){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //사용자가 권한 허용을 눌렀을 경우
                Log.v(TAG,"onRequestPermissionsResult()_보안 통과_grantResults[0]=="+grantResults[0]);
                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        }
    }
}
