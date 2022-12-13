package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class passwordRecovery extends AppCompatActivity {
    private String email;
    private EditText emailrec;
    private Button next;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        db = FirebaseFirestore.getInstance();
        emailrec = findViewById(R.id.emailRecovery);
        next = findViewById(R.id.nextRecovery);

        next.setOnClickListener(view -> {
            next();
        });

    }

    public void next(){
        email = emailrec.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailrec.setError("Introduce tu correo");
            emailrec.requestFocus();
        }
        else{
            CollectionReference user = db.collection("users");
            Query idQuery = user.whereEqualTo("Email", email);
            idQuery.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Usuario Encontrado", Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        Intent nextActivity = new Intent(getBaseContext(), passwordRenewal.class);
                        nextActivity.putExtra("uID", id);
                        nextActivity.putExtra("email",email);
                        startActivity(nextActivity);
                    }
                }
                else{
                    Toast.makeText(this, "No existe este usuario", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}