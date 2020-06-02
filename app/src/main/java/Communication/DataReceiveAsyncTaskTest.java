package Communication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import com.example.semiproject.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import model.SensorDataVO;

public class DataReceiveAsyncTaskTest extends AsyncTask<Void, String, String> {
    String TAG="DataReceiveAsyncTaskTest";
    BufferedReader bufferedReader;
    ImageButton ibReceiveData;
    String jsonData;
    String onOff = "";
    SensorDataVO sensorDataVO;
    ObjectMapper objectMapper = new ObjectMapper();

    public DataReceiveAsyncTaskTest(String jsonData, ImageButton ibReceiveData){
        this.jsonData=jsonData;
        this.ibReceiveData=ibReceiveData;
    }
    /**\
     *Thread 처리 Code
     */
    @Override
    protected String doInBackground(Void... voids) {
        while (true){
            try {
                Log.v(TAG,"doInBackground()_readLine()=="+jsonData);
                if(jsonData != null){
                    sensorDataVO =objectMapper.readValue(jsonData, SensorDataVO.class);
                    //Log.v(TAG,"testVo.getOnOff=="+ sensorDataVO.getOnOff());
                    JSONObject jsonObject = new JSONObject(jsonData);
                    onOff = jsonObject.getString("onOff");
                }
                publishProgress(onOff);
            }catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Thread가 진행 중 실행 UI를 변경할 수 있다.
     * Thread 진행중 bufferedReader를 통해 받아온 데이터를 TextView에 Set해준다
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.v(TAG,"nProgressUpdate_values="+values[0]);
        if(onOff.equals("1")){
            ibReceiveData.setBackgroundResource(R.drawable.window2);
        }else{
            ibReceiveData.setBackgroundResource(R.drawable.window1);
        }
        Log.v(TAG,"getText()=="+ values[0]);
    }
    /**
     * doInBackground 의 수행이 끝난뒤 실행
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.v(TAG,"onPostExecute------------");
    }
}
