package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class NewDietSnacks extends AppCompatActivity {
    private String uID, planID;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private ImageButton addSnack;
    private Button guardar, omitir;
    private LinearLayout snackLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_snacks);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uID = nyauth.getUid();

        addSnack = findViewById(R.id.addSnack);
        guardar = findViewById(R.id.guardar);
        omitir = findViewById(R.id.omitir);
        snackLayout = findViewById(R.id.snacklayout);



        addSnack.setOnClickListener(v -> {
            addSnackOption();
        });

        guardar.setOnClickListener(v -> {

        });

        omitir.setOnClickListener(v -> {

        });
    }

    public void addSnackOption(){
        View singleSnackRow = getLayoutInflater().inflate(R.layout.single_snackrow,null,false);
        EditText description = (EditText) singleSnackRow.findViewById(R.id.inputDescribe);
        ImageButton deleteOption = (ImageButton) singleSnackRow.findViewById(R.id.deleteSnackRow);
        deleteOption.setOnClickListener(v -> {
            removeSnackOption(singleSnackRow);
        });
        snackLayout.addView(singleSnackRow);
    }

    public void removeSnackOption(View v){
        snackLayout.removeView(v);
    }
}