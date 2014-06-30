package com.tundem.holokitkatdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SampleFragment extends Fragment {

    private String title = "";

    public SampleFragment() {

    }

    public SampleFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);

        ((TextView) rootView.findViewById(R.id.txtLabel)).setText(title);

        return rootView;
    }
}
