package com.sergioloc.hologram.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.sergioloc.hologram.R;


public class Step4Fragment extends Fragment {

    private Display display;
    private TextView titleStep4, desStep4;
    private ImageView imgStep4;
    private int width, height;
    private float diagonal;
    private double titleSize, desSize;
    private Context context;

    // newInstance constructor for creating fragment with arguments
    public static Step4Fragment newInstance() {
        Step4Fragment step4Fragment = new Step4Fragment();
        return step4Fragment;
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
        View view = inflater.inflate(R.layout.fragment_step4, container, false);

        context = getContext();


        return view;
    }

}