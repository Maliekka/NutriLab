package com.example.nutrilab.Nutricionist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutrilab.NewDiet;
import com.example.nutrilab.R;


public class nutriDietsFragment extends Fragment {

    Button toCreateActivity;

    public nutriDietsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutri_diets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toCreateActivity = getView().findViewById(R.id.createDieta);
        toCreateActivity.setOnClickListener(v -> {
            Intent toCreate = new Intent(getContext(), NewDiet.class);
            startActivity(toCreate);
        });


    }
}