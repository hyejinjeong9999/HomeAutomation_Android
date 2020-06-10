package recyclerViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.util.ArrayList;

import model.SensorDataVO;

public class SettingLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    View view;
    private ArrayList<SensorDataVO> list;

    public class LogList extends RecyclerView.ViewHolder{
        ImageView iv_log_icon;
        TextView iv_log_message;

        TextView iv_log_timestamp;

        public LogList(@NonNull View itemView) {
            super(itemView);
            iv_log_icon = itemView.findViewById(R.id.iv_log_icon);
            iv_log_message = itemView.findViewById(R.id.iv_log_message);
            iv_log_timestamp = itemView.findViewById(R.id.iv_log_timestamp);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingLogAdapter.LogList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
