package RecyclerViewAdapter;

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

import model.SystemInfoVO;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.SystemInfo>{
    String TAG = "VerticalAdapter";
    Context context;
    ArrayList<SystemInfoVO> list;
    int viewType =0;

    public VerticalAdapter(Context context, ArrayList<SystemInfoVO> list, int viewType) {
        this.context = context;
        this.list = list;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public SystemInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.v(TAG,"onCreateViewHolder_viewTpe=="+viewType);
//        if(viewType == ViewType.ItemVertical){
            view = inflater.inflate(R.layout.recycler_item_systeminfo,parent,false);
            return  new SystemInfo(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull SystemInfo holder, int position) {
            holder.tvSystemName.setText(list.get(position).getTitle());
            holder.tvSituation.setText(list.get(position).getSituation());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SystemInfo extends RecyclerView.ViewHolder{
        public TextView tvSystemName;
        public TextView tvSituation;

        public SystemInfo(@NonNull View itemView) {
            super(itemView);
            tvSystemName=itemView.findViewById(R.id.tvSystemName);
            tvSituation=itemView.findViewById(R.id.tvSituation);
        }
    }
}
