package Communication;

import android.util.Log;

import java.util.LinkedList;

public class SharedObject {
    String TAG = "SharedObject";
    Object monitor = new Object();
    private LinkedList<String> dataList = new LinkedList<>();

    public void put(String msg) {
        synchronized (monitor){
            Log.v(TAG, "put() == " + msg);
            dataList.addLast(msg);
            monitor.notify();
        }
    }

    public String pop() {
        String result = "";
        synchronized (monitor) {
            if (dataList.isEmpty()) {
                try{
                    monitor.wait();
                    result = dataList.removeFirst();

                }catch (InterruptedException e){
                    Log.v(TAG,"pop()_InterruptedException=="+e);
                }
            }else {
                Log.v(TAG,"pop() =="+dataList.getFirst());
                result = dataList.removeFirst();
            }
        }
        return result;
    }
}
