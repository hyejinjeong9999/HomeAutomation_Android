package Communication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;

public class DataReceveAsyncTask extends AsyncTask<Void, String, String> {
    String TAG="DataReceiveAsyncTask";
    BufferedReader bufferedReader;
    String msg = "";

    public DataReceveAsyncTask(BufferedReader bufferedReader){
        this.bufferedReader=bufferedReader;
    }
    /**\
     *Thread 처리 Code
     */
    @Override
    protected String doInBackground(Void... voids) {
        while (true){
            try {
                msg = bufferedReader.readLine();
                Log.v(TAG,"doInBackground()_readLine()=="+msg);
                publishProgress(msg);
            }catch (IOException e){
                Log.v(TAG,"doInBackground()_IOException=="+e);
            }
        }
    }
    /**
     * Thread가 진행 중 실행 UI를 변경할 수 있다.
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.v(TAG,"nProgressUpdate_values="+values[0]);
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
