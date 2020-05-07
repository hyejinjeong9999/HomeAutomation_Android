package RecyclerViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import model.SystemInfoVO;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    String TAG = "VerticalAdapter";
    Context context;
    View view;
    BufferedReader bufferedReader;
    Communication.SharedObject sharedObject;
    ArrayList<SystemInfoVO> list;

    public VerticalAdapter(Context context, ArrayList<SystemInfoVO> list,Communication.SharedObject sharedObject, BufferedReader bufferedReader) {
        this.context = context;
        this.list = list;
        this.bufferedReader = bufferedReader;
        this.sharedObject = sharedObject;
        Log.v(TAG,"list0=="+list.get(0).getViewType());
        Log.v(TAG,"list0=="+list.get(1).getViewType());
        Log.v(TAG,"list0=="+list.get(2).getViewType());
    }
    /**
     * getItemViewType() method에서 Return 받는 VIewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.v(TAG,"onCreateViewHolder()_viewType=="+viewType);
        if(viewType == ViewType.ItemVertical){
            view = inflater.inflate(R.layout.recycler_item_systeminfo,parent,false);
            return new SystemInfo(view);
        }else if(viewType == ViewType.ItemVerticalWeather){
            view = inflater.inflate(R.layout.recycler_item_weatherinfo,parent,false);
            return new SystemInfoWeather(view);
        }else{
            view = inflater.inflate(R.layout.recycler_item_systeminfo_air,parent,false);
            return new SystemInfoSwitch(view);
        }
    }

    /**
     * Position에 해당하는 데이터를 ViewHolder의 아이템뷰에 표시
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Log.v(TAG,"onBindViewHolder()"+holder.itemView);
        if (holder instanceof SystemInfo){
            ((SystemInfo)holder).ivTitle.setImageResource(list.get(position).getImageView());
            ((SystemInfo)holder).tvSystemName.setText(list.get(position).getTitle());
            ((SystemInfo)holder).tvSituation.setText(list.get(position).getSituation());
        }else if (holder instanceof SystemInfoSwitch){
            ((SystemInfoSwitch)holder).ivTitle.setImageResource(list.get(position).getImageView());
            ((SystemInfoSwitch)holder).tvSystemName.setText(list.get(position).getTitle());
            /**
             * SwitchComponent ListenerEvent (Switch Check 상태에 따라 Logic 처리 가능)
             */
            ((SystemInfoSwitch)holder).swSituation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v(TAG, "onCheckedChanged/" + isChecked);
                    if(isChecked == true){
                        sharedObject.put("/aircon ON");
                    }else {
                        sharedObject.put("/aircon OFF");
                    }
                }
            });
        }else if (holder instanceof SystemInfoWeather){
            ((SystemInfoWeather)holder).iv1.setImageResource(list.get(position).getImageView());
            ((SystemInfoWeather)holder).iv2.setImageResource(list.get(position).getImageView());
            ((SystemInfoWeather)holder).iv3.setImageResource(list.get(position).getImageView());
            ((SystemInfoWeather)holder).tv5.setText(list.get(position).getTitle());
        }
        /**
         * //RecyclerView Touch Event (ItemVIew Click시 해당 Item에 Logic처리 가능)//
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"onBindViewHolder()_onClick()_position=="+position);
            }
        });
    }

    /**
     * Item 항목에 맞는 ViewType 값을 Return
     * @param position
     */
    @Override
    public int getItemViewType(int position) {
        Log.v(TAG,"getItemViewType()"+list.get(position).getViewType());
        return list.get(position).getViewType();
    }

    /**
     * 전체 아이템 갯수 리턴
     */
    @Override
    public int getItemCount() {
        Log.v(TAG,"getItemCount()"+list.size());
        return list.size();
    }

    //ItemVIew Class//
    public class SystemInfo extends RecyclerView.ViewHolder{
        public ImageView ivTitle;
        public TextView tvSystemName;
        public TextView tvSituation;

        public SystemInfo(@NonNull View itemView) {
            super(itemView);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvSystemName=itemView.findViewById(R.id.tvSystemName);
            tvSituation=itemView.findViewById(R.id.tvSituation);
            Log.v(TAG,"SystemInfo.class");
        }
    }
    public class SystemInfoSwitch extends RecyclerView.ViewHolder{
        public ImageView ivTitle;
        public TextView tvSystemName;
        public Switch swSituation;

        public SystemInfoSwitch(@NonNull View itemView) {
            super(itemView);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvSystemName=itemView.findViewById(R.id.tvSystemName);
            swSituation=itemView.findViewById(R.id.swSituation);
        }
    }

    public class SystemInfoWeather extends RecyclerView.ViewHolder{
        public ImageView iv1;
        public ImageView iv2;
        public ImageView iv3;
        public TextView tv5;
        public SystemInfoWeather(@NonNull View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.iv1);
            iv2 = itemView.findViewById(R.id.iv2);
            iv3 = itemView.findViewById(R.id.iv3);
            tv5 = itemView.findViewById(R.id.tv5);
        }
    }
}
