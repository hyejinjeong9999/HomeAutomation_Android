package ViewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.io.BufferedReader;
import java.util.ArrayList;

import RecyclerViewAdapter.VerticalAdapter;
import model.SystemInfoVO;
import model.WindowVO;
import model.WeatherVO;

public class FragmentHome extends Fragment {
    String TAG ="FragmentHome";
    View view;
    Context context;
    VerticalAdapter verticalAdapter;

    ArrayList<SystemInfoVO> list;
    WeatherVO weathers;
    WindowVO windowVO;

    GestureDetector gestureDetector;
    Communication.SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentHome(Communication.SharedObject sharedObject, BufferedReader bufferedReader){
        this.sharedObject=sharedObject;
        this.bufferedReader=bufferedReader;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmen_home, container, false);

        /**
         * MainActivity 에서 Bundle을 통해 부여된 Key를 입력하고  Data를 받아온다
         */
        context = container.getContext();
        Bundle bundle = getArguments();
        Log.v(TAG,"bundle=="+(ArrayList<SystemInfoVO>)getArguments().get("list"));
        list=(ArrayList<SystemInfoVO>)getArguments().get("list");
        Log.v(TAG,"weathers=="+(WeatherVO) getArguments().get("weather"));
        weathers = (WeatherVO) getArguments().get("weather");
        Log.v(TAG,"weather.getTemp=="+weathers.getTemp());
        windowVO = (WindowVO) getArguments().get("window");
//        Log.v(TAG,"weather.getOnOff()=="+windowVO.getOnOff());
//        Log.v(TAG,"bundle=="+bundle.getSerializable("weather").toString());
//        weathers=(WeatherVO)bundle.getSerializable("weather");

        /**
         *
         */
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            //누르고 땔 때 한번만 인식하게 하는 처리
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        /**
         * RecyclerVIew 생성 Code
         */
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewVertical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
//        verticalAdapter = new VerticalAdapter(context, list, sharedObject,bufferedReader);
        verticalAdapter = new VerticalAdapter(context, list, weathers, sharedObject,bufferedReader, windowVO);
//        recyclerView.addOnItemTouchListener(onItemTouchListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(verticalAdapter);

        return view;
    }

//    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
//        @Override
//        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//            //손으로 터치한 곳의 좌표를 읽어 해당 Item 의  View를 가져옴.
//            view = rv.findChildViewUnder(e.getX(),e.getY());
//
//            //터치한 곳의 View가 RecyclerView 안의 아이템이고 그 아이템의 View가 null이 아니라
//            //정확한 Item의 View를 가져왔고, gestureDetector에서 한번만 누르면 true를 넘기게 구현했으니
//            //한번만 눌려서 그 값이 true가 넘어왔다면
//            if(view != null && gestureDetector.onTouchEvent(e)){
//                int currentPosition = rv.getChildAdapterPosition(view);
//                Log.v(TAG,"onInterceptTouchEvent()_currentPosition=="+currentPosition);
//                return  true;
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//        }
//    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"FragmentHomeonResumeonStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"FragmentHomeonResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"FragmentHomeonPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"FragmentHomeonStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG,"FragmentHomeonDestroyView");
    }
}
