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


public class Step2Fragment extends Fragment {

    private Display display;
    private TextView titleStep2, desStep2;
    private ImageView imgStep2;
    private int width, height;
    private float diagonal;
    private double titleSize, desSize;
    private Context context;


    // newInstance constructor for creating fragment with arguments
    public static Step2Fragment newInstance() {
        Step2Fragment step2Fragment = new Step2Fragment();
        return step2Fragment;
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
        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        context = getContext();

        return view;
    }
}