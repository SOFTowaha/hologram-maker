package com.sergioloc.hologram.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sergioloc.hologram.R;


public class Step1Fragment extends Fragment {

    public static Step1Fragment newInstance() {
        Step1Fragment fragmentFirst = new Step1Fragment();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step1, container, false);
    }
}
