package Communication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.semiproject.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import model.WeatherVO;

public class WeatherService extends Service {
    String TAG = "WeatherService";
    WeatherVO weathervo = new WeatherVO();
    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String keyword = intent.getExtras().getString("keyword");

        //netword연결을 수행하므로 thread를 이용한다
        WeatherSearchRunnable runnable = new WeatherSearchRunnable();
        Thread t = new Thread(runnable);
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class WeatherSearchRunnable implements Runnable{

        WeatherSearchRunnable( ){
        }

        @Override
        public void run() {
//            String url = "http://70.12.229.165:8080/HomeAutomationWebServer/Weather";
            String url = "http://192.168.1.13:8080/HomeAutomationWebServer/Weather";
            try{
                //1. URL 객체 생성
                URL obj = new URL(url);
                //2. URL Connection 을 개방
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                //3. 연결에 대한 설ㅈ어
                //대표적인 호출방식(GET, POST), 인증에 대한 처리
                con.setRequestMethod("GET");
                //데이터 연결통로 만들어 데이터를 읽어드린다
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }
                Log.v(TAG,"weather=="+stringBuffer.toString());
                bufferedReader.close();
                //value값을 객체화 시킨다
                ObjectMapper mapper = new ObjectMapper();
                WeatherVO[] resultArr = mapper.readValue(stringBuffer.toString(), WeatherVO[].class);
                Log.v(TAG,"resultArr=="+resultArr[0].getTemp());
//                weathervo = mapper.readValue(stringBuffer.toString(), WeatherVO.class);
//                Log.v(TAG,"weathervo=="+weathervo.getTemp());

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("weatherResult", resultArr);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(resultIntent);

            }catch (MalformedURLException e){
                Log.v(TAG, "Wrong URL Exception=="+e.toString());
            }catch (SocketTimeoutException e){
                Log.v(TAG, "TimeOut Exception=="+e.toString());
            }catch (IOException e){
                Log.v(TAG, "Network err Exception=="+e.toString());
            }catch (Exception e){
                Log.v(TAG, "Other Exception=="+e.toString());
            }
        }
    }
}
