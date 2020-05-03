package ViewPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject.R;

public class FragmentA extends Fragment {
    String TAG="FragmentA";
    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a,container,false);
        context=container.getContext();

        return  view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG,"onDetach");
    }
}
