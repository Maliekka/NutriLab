package com.example.nutrilab.Nutricionist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nutrilab.NewDiet;
import com.example.nutrilab.Pacient.PatientModels;
import com.example.nutrilab.PlanModels;
import com.example.nutrilab.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class nutriDietsFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth nyauth;
    Button toCreateActivity;
    RecyclerView plansList;
    private String uID;
    private FirestoreRecyclerAdapter adapter;
    List<PlanModels> a = new ArrayList<>();


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
        plansList = getView().findViewById(R.id.recyclerPlans);
        db = FirebaseFirestore.getInstance();
        nyauth = FirebaseAuth.getInstance();
        uID=nyauth.getUid();

        DocumentReference reference  = db.collection("users").document(uID);
        Query q = db.collection("NutriPlans").whereEqualTo("RefNutri",reference);



        FirestoreRecyclerOptions<PlanModels> options = new FirestoreRecyclerOptions.Builder<PlanModels>()
                .setQuery(q,PlanModels.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PlanModels, PlanModelsViewHolder>(options) {

            @NonNull
            @Override
            public PlanModelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid_planrow,parent, false);
                return new PlanModelsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlanModelsViewHolder holder, int position, @NonNull PlanModels model) {
                holder.NombreView.setText(model.getPaciente());
                holder.FechaView.setText(model.getFecha().toString());
                holder.EditButton.setOnClickListener(v -> {

                });

                holder.itemView.setOnClickListener(v -> {

                });
            }
        };

        plansList.setHasFixedSize(true);
        plansList.setLayoutManager(new nutriDietsFragment.WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        plansList.setAdapter(adapter);

        toCreateActivity = getView().findViewById(R.id.createDieta);
        toCreateActivity.setOnClickListener(v -> {
            Intent toCreate = new Intent(getContext(), NewDiet.class);
            startActivity(toCreate);
        });


    }

    private class PlanModelsViewHolder extends RecyclerView.ViewHolder{
        private TextView NombreView, FechaView;
        private ImageButton EditButton;

        public PlanModelsViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreView = itemView.findViewById(R.id.singlePlanPName);
            FechaView = itemView.findViewById(R.id.singlePlanPDate);
            EditButton = itemView.findViewById(R.id.editPlanRow);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
}