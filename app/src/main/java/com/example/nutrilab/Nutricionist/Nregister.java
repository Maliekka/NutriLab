package com.example.nutrilab.Nutricionist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrilab.R;
import com.example.nutrilab.securityQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Nregister extends AppCompatActivity {
    private EditText name, lname, nced, email, pass;
    private Button register;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private String userID;
    private String q = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nregister);
        name = findViewById(R.id.rNName);
        lname = findViewById(R.id.rlNName);
        nced = findViewById(R.id.rNcedula);
        email = findViewById(R.id.rNemail);
        pass = findViewById(R.id.rNpass);
        register = findViewById(R.id.register);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(view -> {
            createUser();
        });
    }

    public void createUser() {
        String fmail = email.getText().toString();
        String fpassword = pass.getText().toString();
        String fname = name.getText().toString();
        String flname = lname.getText().toString();
        String fnced = nced.getText().toString();

        if (TextUtils.isEmpty(fmail)) {
            email.setError("Llena todos los campos");
            email.requestFocus();
        } else if (TextUtils.isEmpty(fpassword)) {
            pass.setError("Llena todos los campos");
            pass.requestFocus();
        } else if (TextUtils.isEmpty(fname)) {
            name.setError("Llena todos los campos");
            name.requestFocus();
        } else if (TextUtils.isEmpty(flname)) {
            lname.setError("Llena todos los campos");
            lname.requestFocus();
        } else if (TextUtils.isEmpty(fnced)) {
            nced.setError("Llena todos los campos");
            nced.requestFocus();
        } else {
            nyauth.createUserWithEmailAndPassword(fmail, fpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userID = nyauth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        Map<String,Object> NutriologoData = new HashMap<>();
                        NutriologoData.put("nCedula", fnced);
                        NutriologoData.put("About","Mas información acerca del nutriólogo.");
                        user.put("Nombre", fname);
                        user.put("Apellidos", flname);
                        user.put("Email", fmail);
                        user.put("Password", fpassword);
                        user.put("securityQuestion", q);
                        user.put("securityAnswer", q);
                        user.put("Type", 1);
                        user.put("statusAccount",0);
                        user.put("regTimestamp", new Date());
                        user.put("nutriologoData", NutriologoData);
                        user.put("profilePic","");
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Usuario registrado con éxito" + userID);
                                Intent sq = new Intent(Nregister.this, securityQuestion.class);
                                startActivity(sq);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(Nregister.this, "Lo sentimos, no se pudo registrar el usuario" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}