package ViewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiproject.R;

import java.util.ArrayList;

import RecyclerViewAdapter.VerticalAdapter;
import RecyclerViewAdapter.ViewType;
import model.SystemInfoVO;

public class FragmentHome extends Fragment {
    String TAG ="FragmentHome";
    View view;
    Context context;

    VerticalAdapter verticalAdapter;
    RecyclerView recyclerView;

    ArrayList<SystemInfoVO> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmen_home, container, false);
        context = container.getContext();

//        list.add(new SystemInfoVO("home","wjdtkd", ViewType.ItemVertical));
//        list.add(new SystemInfoVO("home","wjdtkd", ViewType.ItemVertical));
//        list.add(new SystemInfoVO("home","wjdtkd", ViewType.ItemVertical));

        Bundle bundle = getArguments();
//        list = bundle.getParcelableArrayList("list");
        Log.v(TAG,"bundle=="+(ArrayList<SystemInfoVO>)getArguments().get("list"));
        list=(ArrayList<SystemInfoVO>)getArguments().get("list");
        Log.v(TAG,"list=="+list);

        recyclerView = view.findViewById(R.id.recyclerViewVertical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
        verticalAdapter = new VerticalAdapter(context, list, 0);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(verticalAdapter);
        return view;
    }
}
