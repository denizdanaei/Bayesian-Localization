package com.example.k2.d2.k2d2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.k2.d2.k2d2.bayesianLocalization.localize;
public class tab_localize extends Fragment {
    private static final String TAG= "Localize";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.tab_localize, container, false);
        return view;
    }
}
