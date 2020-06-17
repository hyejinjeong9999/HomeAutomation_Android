package recyclerViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.util.ArrayList;
import java.util.List;

import communication.SharedObject;
import model.LogVO;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogInfomation> {
    String TAG = "LogAdapter";
    Context context;
    View view;
    SharedObject sharedObject;

    LogVO logVO;
    String logDataStatus;

//    recyclerViewCall("Airconditioner");
//                    break;
//                case R.id.btnAirpurifier:
//    recyclerViewCall("Airpurifier");
//                    break;
//                case R.id.btnWindow:
//    recyclerViewCall("Window");

    public LogAdapter(Context context, SharedObject sharedObject, LogVO logVO, String logDataStatus){
        this.context = context;
        this.sharedObject = sharedObject;
        this.logVO = logVO;
        this.logDataStatus = logDataStatus;
    }

    @NonNull
    @Override
    public LogInfomation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG,"onCreateViewHolder");
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.recycler_item_loglist,parent,false);
        return new LogInfomation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogInfomation holder, int position) {
        Log.v(TAG,"onBindViewHolder======="+logDataStatus);
        Log.v(TAG,"logDataStatus=="+logDataStatus);
        if (logDataStatus .equals("Airconditioner") ){
            ((LogInfomation)holder).tvSituation.setText(logVO.getAirconditionerList().get(position).getAirconditionerStatus());
            ((LogInfomation)holder).tvDate.setText(logVO.getAirconditionerList().get(position).getAirconditionerTime());
        }else if (logDataStatus.equals("Airpurifier")){
            ((LogInfomation)holder).tvSituation.setText(logVO.getAirpurifierList().get(position).getAirpurifierStatus());
            ((LogInfomation)holder).tvDate.setText(logVO.getAirpurifierList().get(position).getAirpurifierTime());
        }else if (logDataStatus == "Window"){
            ((LogInfomation)holder).tvSituation.setText(logVO.getWindowList().get(position).getWindowStatus());
            ((LogInfomation)holder).tvDate.setText(logVO.getWindowList().get(position).getWindowTime());
        }else if (logDataStatus == "Airconditioner"){
            ((LogInfomation)holder).tvSituation.setText(logVO.getAirconditionerList().get(position).getAirconditionerStatus());
            ((LogInfomation)holder).tvDate.setText(logVO.getAirconditionerList().get(position).getAirconditionerTime());
        }else if (logDataStatus == "Airconditioner"){
            ((LogInfomation)holder).tvSituation.setText(logVO.getAirconditionerList().get(position).getAirconditionerStatus());
            ((LogInfomation)holder).tvDate.setText(logVO.getAirconditionerList().get(position).getAirconditionerTime());
        }
    }

    @Override
    public int getItemCount() {
        if (logDataStatus.equals("Airconditioner")){
            Log.v(TAG,"getAirconditionerList=="+logVO.getAirconditionerList().size());
            return logVO.getAirconditionerList().size();
        }else if (logDataStatus == "Airpurifier"){
            Log.v(TAG,"getAirpurifierList=="+logVO.getAirpurifierList().size());
            return logVO.getAirpurifierList().size();
        }else if (logDataStatus == "Window"){
            Log.v(TAG,"getWindowList=="+logVO.getWindowList().size());
            return logVO.getWindowList().size();
        }else if (logDataStatus == "xxxxx"){
            Log.v(TAG,"getAirpurifierList=="+logVO.getAirpurifierList().size());
            return logVO.getAirpurifierList().size();
        }else{
            return 0;
        }
    }

    public class LogInfomation extends RecyclerView.ViewHolder{
        TextView tvSituation;
        TextView tvDate;

        public LogInfomation(@NonNull View itemView){
            super(itemView);
            tvSituation = itemView.findViewById(R.id.tvSituation);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}

