package recyclerViewAdapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import communication.SharedObject;
import model.SensorDataVO;
import model.SystemInfoVO;
import model.WeatherVO;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "VerticalAdapter";
    Context context;
    View view;
    LayoutInflater inflater;
    BufferedReader bufferedReader;
    SharedObject sharedObject;
    ArrayList<SystemInfoVO> itemList;
    WeatherVO weathers;
    SensorDataVO sensorDataVO;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    //Item 의 클릭 상태를 저장 하는 ArrayObject
    SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    int oldPosition;
    int lastPosition;
    // Item Position clicked before
    int prePosition = -1;
    ViewGroup myParent;

    public VerticalAdapter(
            Context context, ArrayList<SystemInfoVO> itemList, WeatherVO weathers,
            SharedObject sharedObject, BufferedReader bufferedReader, SensorDataVO sensorDataVO) {
        this.context = context;
        this.itemList = itemList;
        this.weathers = weathers;
        this.bufferedReader = bufferedReader;
        this.sharedObject = sharedObject;
        this.sensorDataVO = sensorDataVO;
    }

    /**
     * getItemViewType() method에서 Return 받는 VIewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myParent = parent;
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.v(TAG, "onCreateViewHolder()_viewType==" + viewType);
        if (viewType == ViewType.ItemVertical) {
            view = inflater.inflate(R.layout.recycler_item_systeminfo, parent, false);
            return new SystemInfo(view);
        } else if (viewType == ViewType.ItemVerticalWeather) {
            view = inflater.inflate(R.layout.recycler_item_weatherinfo, parent, false);
            return new SystemInfoWeather(view);
        } else {
            view = inflater.inflate(R.layout.recycler_item_systeminfo_air, parent, false);
            return new SystemInfoSwitch(view);
        }
    }

    /**
     * Position에 해당하는 데이터를 ViewHolder의 아이템뷰에 표시
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.v(TAG, "onBindViewHolder()" + holder.itemView);
        if (holder instanceof SystemInfo) {
            Log.v(TAG, "" + itemList.get(position).getTitle());
            if (itemList.get(position).getTitle().equals("냉장고")) {
                ((SystemInfo) holder).ivTitle.setImageResource(itemList.get(position).getImageView());
                ((SystemInfo) holder).tvSystemName.setText(itemList.get(position).getTitle());
                ((SystemInfo) holder).tvSituation.setText("장고장고");

            } else {
                ((SystemInfo) holder).ivTitle.setImageResource(itemList.get(position).getImageView());
                ((SystemInfo) holder).tvSystemName.setText(itemList.get(position).getTitle());
                ((SystemInfo) holder).tvSituation.setText(itemList.get(position).getSituation());
            }
//            int deviceWidth = displayMetrics.widthPixels;  // 핸드폰의 가로 해상도를 구함.
//            deviceWidth = deviceWidth / 2;
//            int deviceHeight = (int) (deviceWidth * 1.5);
//            holder.itemView.getLayoutParams().height = deviceHeight;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
//
//            holder.itemView.requestLayout(); // 변경 사항 적용
        } else if (holder instanceof SystemInfoSwitch) {
            ((SystemInfoSwitch) holder).ivTitle.setImageResource(itemList.get(position).getImageView());
            ((SystemInfoSwitch) holder).tvSystemName.setText(itemList.get(position).getTitle());

            /**
             * SwitchComponent ListenerEvent (Switch Check 상태에 따라 Logic 처리 가능)
             */
            if (sensorDataVO.getWindowStatus().equals("0")) {
                ((SystemInfoSwitch) holder).swSituation.setChecked(true);
            } else {
                ((SystemInfoSwitch) holder).swSituation.setChecked(false);
            }
            ((SystemInfoSwitch) holder).swSituation.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Log.v(TAG, "onCheckedChanged/" + isChecked);
                            if (isChecked == true) {
                                sharedObject.put("/ANDROID>/WINDOW ON");
                            } else {
                                sharedObject.put("/ANDROID>/WINDOW OFF");
                            }
                        }
                    });
            // Switch Component onTouch Event (Double Touch 시 호출됨.....)
            ((SystemInfoSwitch) holder).swSituation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "swSituation onClick()");
                }
            });
        } else if (holder instanceof SystemInfoWeather) {
            ((SystemInfoWeather) holder).tvTempOut.setText(weathers.getTemp() + " ℃");
            /**
             * weathers.getWeather() 값에 따라 SystemInfoWeather Item View에 그림 출력
             */
            Log.v(TAG, "getWeather==" + weathers.getWeather());
            if (weathers.getWeather().equals("Drizzle")) {
                ((SystemInfoWeather) holder).ivWeather.setImageResource(R.drawable.rainy);
            } else if (weathers.getWeather().equals("Mist")) {
                ((SystemInfoWeather) holder).ivWeather.setImageResource(R.drawable.mist);
            } else if (weathers.getWeather().equals("Clouds")) {
                ((SystemInfoWeather) holder).ivWeather.setImageResource(R.drawable.cloudy);
            } else if (weathers.getWeather().equals("Rain")) {
                ((SystemInfoWeather) holder).ivWeather.setImageResource(R.drawable.rainy);
            } else {
                ((SystemInfoWeather) holder).ivWeather.setImageResource(R.drawable.sunny);
            }
            Log.v(TAG, "================================" + weathers.getPm10Value());
            double dustDensityOut = Double.parseDouble(weathers.getPm10Value());
            if (dustDensityOut <= 15) {
                ((SystemInfoWeather) holder).ivDustOut.setImageResource(R.drawable.ic_dusty_verygood);
            } else if (dustDensityOut <= 35 && dustDensityOut > 15) {
                ((SystemInfoWeather) holder).ivDustOut.setImageResource(R.drawable.ic_dusty_good);
            } else if (dustDensityOut <= 75 && dustDensityOut > 35) {
                ((SystemInfoWeather) holder).ivDustOut.setImageResource(R.drawable.ic_dusty_bad);
            } else {
                ((SystemInfoWeather) holder).ivDustOut.setImageResource(R.drawable.ic_dusty_verybad);
            }
            Log.v(TAG, "weathers.getPm10Value() " + weathers.getPm10Value());
            Log.v(TAG, "weathers.getPm10Value() " + weathers.getPm25Value());
            ((SystemInfoWeather) holder).tvDustOutSituation.setText(weathers.getPm10Value() + " μg/m³");

            Log.v(TAG, "TEST==" + sensorDataVO.getTemp());
            ((SystemInfoWeather) holder).tvTempIn.setText(sensorDataVO.getTemp() + " ℃");
            ((SystemInfoWeather) holder).tvModeSituation.setText(sensorDataVO.getMode());
            if (sensorDataVO.getMode().equals("OFF")) {
                oldPosition = position;
            } else if (sensorDataVO.getMode().equals("SMART")) {
                lastPosition = 1;
                oldPosition = position;
            } else if (sensorDataVO.getMode().equals("SLEEP")) {
                lastPosition = 2;
                oldPosition = position;
            } else if (sensorDataVO.getMode().equals("OUTING")) {
                lastPosition = 3;
                oldPosition = position;
            }


            double dustDensity = Double.parseDouble(sensorDataVO.getDust10());
            if (dustDensity <= 15) {
                ((SystemInfoWeather) holder).ivDust.setImageResource(R.drawable.ic_dusty_verygood);
            } else if (dustDensity <= 35 && dustDensity < 15) {
                ((SystemInfoWeather) holder).ivDust.setImageResource(R.drawable.ic_dusty_good);
            } else if (dustDensity <= 75 && dustDensity < 35) {
                ((SystemInfoWeather) holder).ivDust.setImageResource(R.drawable.ic_dusty_bad);
            } else {
                ((SystemInfoWeather) holder).ivDust.setImageResource(R.drawable.ic_dusty_verybad);
            }
            ((SystemInfoWeather) holder).tvSituation.setText(sensorDataVO.getDust10() + " μg/m³");
        }
        /**
         * //RecyclerView Touch Event (ItemVIew Click시 해당 Item에 Logic처리 가능)//
         */
        if (lastPosition == position && position != 0) {
            ((SystemInfo) holder).layoutSystemInfo.setBackgroundResource(R.drawable.round_border_cliked);
            ((SystemInfo) holder).tvSystemName.setTextColor(context.getResources().getColor(R.color.fontDark, null));
        } else if (oldPosition == position && position != 0) {
            ((SystemInfo) holder).layoutSystemInfo.setBackgroundResource(R.drawable.round_border);
            ((SystemInfo) holder).tvSystemName.setTextColor(context.getResources().getColor(R.color.recyclerViewItemFont, null));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onBindViewHolder()_onClick()_position==" + position);
                String mode = "";
                switch (position) {
                    case 1:
                        mode = "SMART";
                        changeBackgoround(position, mode);
                        break;
                    case 2:
                        mode = "SLEEP";
                        changeBackgoround(position, mode);
                        break;
                    case 3:
                        mode = "QUTING";
                        changeBackgoround(position, mode);
                        break;
                    default:
                }
            }
        });
    }

    public void changeBackgoround(int position, String mode) {
        if (lastPosition == position) {
            sharedObject.put("/ANDROID>/MODE OFF");
            oldPosition = lastPosition;
            lastPosition = 999;
            notifyItemChanged(oldPosition);
            notifyItemChanged(lastPosition);
        } else {
            sharedObject.put("/ANDROID>/MODE "+mode);
            oldPosition = lastPosition;
            lastPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(lastPosition);
        }
    }

    /**
     * Item 항목에 맞는 ViewType 값을 Return
     *
     * @param position
     */
    @Override
    public int getItemViewType(int position) {
        Log.v(TAG, "getItemViewType()" + itemList.get(position).getViewType());
        return itemList.get(position).getViewType();
    }

    /**
     * 전체 아이템 갯수 리턴
     */
    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount()" + itemList.size());
        return itemList.size();
    }

    //////////ItemVIew Class//////////
    public class SystemInfo extends RecyclerView.ViewHolder {
        public ImageView ivTitle;
        public TextView tvSystemName;
        public TextView tvSituation;
        public LinearLayout layoutSystemInfo;

        public SystemInfo(@NonNull View itemView) {
            super(itemView);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvSystemName = itemView.findViewById(R.id.tvSystemName);
            tvSituation = itemView.findViewById(R.id.tvSituation);
            layoutSystemInfo = itemView.findViewById(R.id.layoutSystemInfo);
            Log.v(TAG, "SystemInfo.class");
        }
    }

    public class SystemInfoSwitch extends RecyclerView.ViewHolder {
        public ImageView ivTitle;
        public TextView tvSystemName;
        public Switch swSituation;

        public SystemInfoSwitch(@NonNull View itemView) {
            super(itemView);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvSystemName = itemView.findViewById(R.id.tvSystemName);
            swSituation = itemView.findViewById(R.id.swSituation);
        }
    }

    public class SystemInfoWeather extends RecyclerView.ViewHolder {
        public TextView tvTempIn;
        public ImageView ivWeather;
        public ImageView ivDustOut;
        public TextView tvDustOutSituation;

        public TextView tvTempOut;
        public TextView tvModeSituation;
        public ImageView ivDust;
        public TextView tvSituation;

        public SystemInfoWeather(@NonNull View itemView) {
            super(itemView);
//            tvTemp = itemView.findViewById(R.id.tvTemp);
//            ivWeather = itemView.findViewById(R.id.ivWeather);
//            ivSituation = itemView.findViewById(R.id.ivSituation);
//            tvSituation = itemView.findViewById(R.id.tvSituation);
            tvTempIn = itemView.findViewById(R.id.tvTempIn);
            ivWeather = itemView.findViewById(R.id.ivWeather);
            ivDustOut = itemView.findViewById(R.id.ivDustOut);
            tvDustOutSituation = itemView.findViewById(R.id.tvDustOutSituation);

            tvTempOut = itemView.findViewById(R.id.tvTempOut);
            tvModeSituation = itemView.findViewById(R.id.tvModeSituation);
            ivDust = itemView.findViewById(R.id.ivDust);
            tvSituation = itemView.findViewById(R.id.tvSituation);
        }
    }

    //Test//
    public class SystemInfo1 extends RecyclerView.ViewHolder {
        public ImageView ivTitle;
        public TextView tvSystemName;
        public TextView tvSituation;

        public SystemInfo1(@NonNull View itemView) {
            super(itemView);
            ivTitle = itemView.findViewById(R.id.ivTitle);
            tvSystemName = itemView.findViewById(R.id.tvSystemName);
            tvSituation = itemView.findViewById(R.id.tvSituation);
            Log.v(TAG, "SystemInfo.class");
        }

        public void onBind(SystemInfoVO list, int position, SparseBooleanArray sparseBooleanArray) {
            ivTitle.setImageResource(itemList.get(position).getImageView());
            tvSystemName.setText(itemList.get(position).getTitle());
            tvSituation.setText(itemList.get(position).getSituation());
            changeVisibility(sparseBooleanArray.get(position));
        }

        public void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator valueAnimator = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation 이 실행되는 시간, n/1000초
            valueAnimator.setDuration(600);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    //imageView의 높이 변경
//                imageView2.getLayoutParams().height = value;
//                imageView2.requestLayout();
//                // imageView가 실제로 사라지게하는 부분
//                imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}