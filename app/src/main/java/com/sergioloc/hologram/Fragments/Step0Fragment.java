package com.sergioloc.hologram.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.sergioloc.hologram.R;


public class Step0Fragment extends Fragment {

    private Display display;
    private TextView materials,m1,m2,m3,m4,m5;
    private int width, height;
    private float diagonal;
    private double titleSize, materialsSize;
    private Context context;

    // newInstance constructor for creating fragment with arguments
    public static Step0Fragment newInstance() {
        Step0Fragment step0Fragment = new Step0Fragment();
        return step0Fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step0, container, false);

        context = getContext();




        return view;
    }
}
