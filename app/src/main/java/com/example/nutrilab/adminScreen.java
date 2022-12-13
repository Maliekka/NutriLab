package com.example.nutrilab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Nutricionist.NutriModels;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class adminScreen extends AppCompatActivity {

    private Button logOut;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private RecyclerView nutList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        toolbar();

        logOut = findViewById(R.id.botonchito);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        nutList = findViewById(R.id.recyclerNut);

        Query query = db.collection("users").whereEqualTo("statusAccount", 0);

        FirestoreRecyclerOptions<NutriModels> options = new FirestoreRecyclerOptions.Builder<NutriModels>()
                .setQuery(query,NutriModels.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<NutriModels, NutriModelsViewHolder>(options) {
            @NonNull
            @Override
            public NutriModelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recycler_item,parent, false);
                return new NutriModelsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NutriModelsViewHolder holder, int position, @NonNull NutriModels model) {
                holder.Nombre.setText(model.getNombre());
                holder.Apellidos.setText(model.getApellidos());
                holder.Registro.setText(model.getRegTimestamp().toString());
                holder.Nced.setText(model.getNutriologoData().get("nCedula").toString());
                String o = model.getNutriologoData().get("nCedula").toString();
                Query get = db.collection("users").whereEqualTo("nutriologoData.nCedula",o);
                holder.accept.setOnClickListener(view -> {
                    get.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot:snapshotList){
                            snapshot.getReference().update("statusAccount",1);
                            Toast.makeText(adminScreen.this, "Nutriólogo Aceptado", Toast.LENGTH_SHORT).show();
                        }
                    });

                });

                holder.reject.setOnClickListener(view -> {
                    get.get().addOnSuccessListener(queryDocumentSnapshots -> {
                          List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                          for (DocumentSnapshot snapshot:snapshotList){
                              snapshot.getReference().update("statusAccount",2);
                              Toast.makeText(adminScreen.this, "Nutriólogo Rechazado", Toast.LENGTH_SHORT).show();
                          }
                      });
                });

            }
        };

//
        nutList.setAdapter(adapter);
        nutList.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        logOut.setOnClickListener(view -> {
            nyauth.signOut();
            Toast.makeText(adminScreen.this, "¡Nos vemos!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private class NutriModelsViewHolder extends RecyclerView.ViewHolder {
        private final TextView Nombre,Apellidos,Registro,Nced;
        private final ImageButton accept, reject;
        public NutriModelsViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre=itemView.findViewById(R.id.recyNutName);
            Apellidos=itemView.findViewById(R.id.recyNutLastName);
            Registro=itemView.findViewById(R.id.recyTime);
            Nced=itemView.findViewById(R.id.recyNced);
            accept = itemView.findViewById(R.id.acceptNut);
            reject = itemView.findViewById(R.id.rejectNut);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    protected void onStart() {
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

    public void toolbar(){
        Toolbar adminTB = findViewById(R.id.toolbar);
        setSupportActionBar(adminTB); //pasa a la posición que originalmente ocupaba el action bar
    }

}