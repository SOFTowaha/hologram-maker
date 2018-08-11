package com.sergioloc.hologram.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sergioloc.hologram.R;

public class StepFragment extends Fragment {

    private int step;

    public static StepFragment newInstance(int step) {
        StepFragment fragmentFirst = new StepFragment();
        fragmentFirst.step = step;
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (step == 0){
            return inflater.inflate(R.layout.fragment_step0, container, false);
        }else if (step == 1){
            return inflater.inflate(R.layout.fragment_step1, container, false);
        }else if (step == 2){
            return inflater.inflate(R.layout.fragment_step2, container, false);
        }else if (step == 3){
            return inflater.inflate(R.layout.fragment_step3, container, false);
        }else if (step == 4){
            return inflater.inflate(R.layout.fragment_step4, container, false);
        }else {
            return inflater.inflate(R.layout.fragment_step5, container, false);
        }
    }
}

