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

    public LogAdapter(Context context, SharedObject sharedObject, LogVO logVO){
        this.context = context;
        this.sharedObject = sharedObject;
        this.logVO = logVO;
    }

    @NonNull
    @Override
    public LogInfomation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG,"onCreateViewHolder");
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.recycler_item_loglist,parent,false);
//        view = inflater.inflate(R.layout.recycler_item_systeminfo_air_control, parent, false);
        return new LogInfomation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogInfomation holder, int position) {
        Log.v(TAG,"onBindViewHolder");
        ((LogInfomation)holder).tvSituation.setText(logVO.getAirconditionerList().get(position).getAirconditionerStatus());
        ((LogInfomation)holder).tvDate.setText(logVO.getAirconditionerList().get(position).getAirconditionerTime());
    }

    @Override
    public int getItemCount() {
        Log.v(TAG,"getItemCount=="+logVO.getAirconditionerList().size());
        for (int i = 0; i<logVO.getAirconditionerList().size(); i++){
            Log.v(TAG,"Adapter Test == "+logVO.getAirconditionerList().get(i).getAirconditionerTime());
        }
//        return 1;
        return logVO.getAirconditionerList().size();
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
