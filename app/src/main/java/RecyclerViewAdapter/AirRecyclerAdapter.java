package RecyclerViewAdapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import Communication.SharedObject;
import model.SensorDateVO;
import model.SystemInfoVO;
import model.WeatherVO;

public class AirRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "AirRecyclerAdapter";
    Context context;
    View view;
    private WeatherVO weatherVO;
    private SensorDateVO sensorDateVO;
    private BufferedReader bufferedReader;
    private SharedObject sharedObject;
    private ArrayList<SystemInfoVO> list;
    SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    int prePosition = -1;

    public AirRecyclerAdapter(Context context, SharedObject sharedObject, BufferedReader bufferedReader,
                              SensorDateVO sensorDateVO, WeatherVO weatherVO, ArrayList<SystemInfoVO> list)  {
        this.context = context;
        this.bufferedReader = bufferedReader;
        this.sharedObject = sharedObject;
        this.sensorDateVO = sensorDateVO;
        this.weatherVO = weatherVO;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == ViewType.ItemVerticalAir) {
            view = inflater.inflate(R.layout.recycler_item_airinfo, parent, false);
            return new AirRecyclerAdapter.AirInfo(view);
        }else{
            //recycler_item_systeminfo 다 같은 xml파일인데 수정 사항에 대비해서 xml같은 걸로 하나더 만들었습니다. vvvvv
            //view = inflater.inflate(R.layout.recycler_item_systeminfo, parent, false);
            view = inflater.inflate(R.layout.recycler_item_systeminfo_air_control, parent, false);
            return new AirRecyclerAdapter.AirControl(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AirInfo){
            ((AirInfo)holder).tvPM25In.setText(sensorDateVO.getDustDensity()  + " μg/m³");
            ((AirInfo)holder).tvPM10In.setText("pm10" + " μg/m³");
            ((AirInfo)holder).tvGas.setText("GAS SENSOR");
            ((AirInfo)holder).tvPM25Out.setText(sensorDateVO.getDustDensity()  + " μg/m³");
            ((AirInfo)holder).tvPM10Out.setText(sensorDateVO.getDustDensity()  + " μg/m³");
        }else  if(holder instanceof AirControl){
            ((AirControl)holder).ivAirControl.setImageResource(R.drawable.sunny);
            ((AirControl)holder).ivWindowControl.setImageResource(R.drawable.sunny);
            ((AirControl)holder).tvAirControl.setText("공기청정기");
            ((AirControl)holder).tvWindowControl.setText("창문");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"onBindViewHolder()_onClick()_position=="+position);
                switch (position){
                    case 0:
                        Log.v(TAG,"onBindViewHolder()_onClick()_position=0="+position);
                        break;
                    case 1:
                        Log.v(TAG,"onBindViewHolder()_onClick()_position=1="+position);
                        break;
                    case 3:
                        if (sparseBooleanArray.get(position)) {
                            // 펼쳐진 Item을 클릭 시
                            sparseBooleanArray.delete(position);
                        } else {
                            // 직전의 클릭됐던 Item의 클릭상태를 지움
                            sparseBooleanArray.delete(prePosition);
                            // 클릭한 Item의 position을 저장
                            sparseBooleanArray.put(position, true);
                        }
                        // 해당 포지션의 변화를 알림
                        if (prePosition != -1) notifyItemChanged(prePosition);
                        notifyItemChanged(position);
                        // 클릭된 position 저장
                        prePosition = position;
                        break;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AirInfo extends RecyclerView.ViewHolder {
        TextView tvPM25In;
        TextView tvPM10In;
        TextView tvGas;

        TextView tvPM25Out;
        TextView tvPM10Out;

        public AirInfo(@NonNull View itemView) {
            super(itemView);
            tvPM25In = itemView.findViewById(R.id.tvPM25In);
            tvPM10In = itemView.findViewById(R.id.tvPM10In);
            tvGas = itemView.findViewById(R.id.tvGas);
            tvPM25Out = itemView.findViewById(R.id.tvPM25Out);
            tvPM10Out = itemView.findViewById(R.id.tvPM10Out);
        }
    }

    public class AirControl extends RecyclerView.ViewHolder {
        ImageView ivAirControl;
        ImageView ivWindowControl;
        TextView tvAirControl;
        TextView tvWindowControl;
        Switch swAirControl;
        Switch swWindowControl;

        public AirControl(@NonNull View itemView) {
            super(itemView);
            ivAirControl = itemView.findViewById(R.id.ivAirControl);
            ivWindowControl = itemView.findViewById(R.id.ivWindowControl);
            tvAirControl = itemView.findViewById(R.id.tvAirControl);
            tvWindowControl = itemView.findViewById(R.id.tvWindowControl);
            swAirControl = itemView.findViewById(R.id.swAirControl);
            swWindowControl = itemView.findViewById(R.id.swWindowControl);
        }
    }
}
