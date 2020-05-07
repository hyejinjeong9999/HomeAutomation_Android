package Communication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import model.TestVO;

public class DataReceveAsyncTask111 extends AsyncTask<Void, String, String> {
    String TAG="DataReceiveAsyncTask";
    ObjectInputStream objectInputStream;
    TestVO vo;
    String msg = "";

    public DataReceveAsyncTask111(ObjectInputStream objectInputStream,TestVO vo){
        this.objectInputStream=objectInputStream;
        this.vo=vo;
    }
    /**\
     *Thread 처리 Code
     */
    @Override
    protected String doInBackground(Void... voids) {
        while (true){
            try {
                vo = (TestVO)objectInputStream.readObject();
                Log.v(TAG,"onCreate=="+vo.toString());
//                msg = bufferedReader.readLine();
//                Log.v(TAG,"doInBackground()_readLine()=="+msg);
//                publishProgress(msg);
            }catch (IOException | ClassNotFoundException e){
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
