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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Pacient.PatientModels;
import com.example.nutrilab.R;
import com.example.nutrilab.adminScreen;
import com.example.nutrilab.nutriPatientInfo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.Query;

import java.util.List;

public class nutriPatientsFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth nyauth;
    private RecyclerView patientsRecycler;
    private String uID;
    private FirestoreRecyclerAdapter adapter;


    public nutriPatientsFragment() {
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
        return inflater.inflate(R.layout.fragment_nutri_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        nyauth = FirebaseAuth.getInstance();
        uID=nyauth.getUid();

        patientsRecycler = getView().findViewById(R.id.recyclerPatientsList);
        DocumentReference reference  = db.collection("users").document(uID);

        Query q = db.collection("users").whereEqualTo("NutriLinked",reference);

        FirestoreRecyclerOptions<PatientModels> options = new FirestoreRecyclerOptions.Builder<PatientModels>()
                .setQuery(q,PatientModels.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PatientModels, PatientModelsViewHolder>(options) {
            @NonNull
            @Override
            public PatientModelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerecycler_patients_list,parent, false);
                return new PatientModelsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PatientModelsViewHolder holder, int position, @NonNull PatientModels model) {
                holder.Nombre.setText(model.getNombre());
                holder.Apellidos.setText(model.getApellidos());
                holder.itemView.setOnClickListener(view1 -> {
                    Intent intent = new Intent(getContext(), nutriPatientInfo.class);
                    intent.putExtra("dataPatient",model);
                    startActivity(intent);
                });
            }
        };

        patientsRecycler.setHasFixedSize(false);
        patientsRecycler.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        patientsRecycler.setAdapter(adapter);

}


    private class PatientModelsViewHolder extends RecyclerView.ViewHolder{
        private TextView Nombre,Apellidos;

        public PatientModelsViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre=itemView.findViewById(R.id.patientName);
            Apellidos=itemView.findViewById(R.id.patientLname);
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