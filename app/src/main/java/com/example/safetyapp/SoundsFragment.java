package com.example.safetyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.safetyapp.CallActivity;
import com.example.safetyapp.DogActivity;
import com.example.safetyapp.LaughActivity;
import com.example.safetyapp.R;

import android.content.Intent;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SoundsFragment extends Fragment {

    public SoundsFragment()
    {
        //Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sound, container, false);

        Button Dogs = (Button) rootView.findViewById(R.id.dogButton);

        Button Call = (Button) rootView.findViewById(R.id.callButton);

        Button Laugh = (Button) rootView.findViewById(R.id.laughterButton);

        Dogs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), DogActivity.class);
                startActivity(in);
            }
        });

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), CallActivity.class);
                startActivity(in);
            }
        });

        Laugh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), LaughActivity.class);
                startActivity(in);
            }
        });
        return rootView;
    }
}