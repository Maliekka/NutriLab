package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutrilab.Nutricionist.nutriMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewDietSnacks extends AppCompatActivity {
    private String  planID;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private ImageButton addSnack;
    private Button guardar, omitir;
    private LinearLayout snackLayout;
    private EditText extraData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_snacks);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        addSnack = findViewById(R.id.addSnack);
        guardar = findViewById(R.id.guardar);
        omitir = findViewById(R.id.omitir);
        snackLayout = findViewById(R.id.snacklayout);
        extraData=findViewById(R.id.extraData);

        planID = getIntent().getStringExtra("ID");


        addSnack.setOnClickListener(v -> {
            addSnackOption();
        });

        guardar.setOnClickListener(v -> {
            saveSnacks();
            startActivity(new Intent(this, nutriMenu.class));
        });

        omitir.setOnClickListener(v -> {
            startActivity(new Intent(this,nutriMenu.class));
        });
    }

    public void addSnackOption(){
        View singleSnackRow = getLayoutInflater().inflate(R.layout.single_snackrow,null,false);
        ImageButton deleteOption = (ImageButton) singleSnackRow.findViewById(R.id.deleteSnackRow);
        deleteOption.setOnClickListener(v -> {
            removeSnackOption(singleSnackRow);
        });
        snackLayout.addView(singleSnackRow);
    }

    public void removeSnackOption(View v){
        snackLayout.removeView(v);
    }

    public void saveSnacks(){
        String stringExtraData = extraData.getText().toString();
        List<String> snacks = new ArrayList<>();
        View rowSnack;
        for (int i = 0; i<snackLayout.getChildCount();i++){
            rowSnack=snackLayout.getChildAt(i);
            EditText description  = rowSnack.findViewById(R.id.inputDescribe);
            if(!description.getText().toString().isEmpty()){// verificar si añadió una descripción vacia, si es asi, simplemente no lo añade a la lista
                snacks.add(description.getText().toString());
            }
        }
        DocumentReference documentReference = db.collection("NutriPlans").document(planID);
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    documentReference.update("colaciones",snacks);
                    if(!stringExtraData.isEmpty()){
                        documentReference.update("comentario",stringExtraData);
                    }
                }
            }
        });
    }
}