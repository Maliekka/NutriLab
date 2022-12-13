package com.example.nutrilab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class passwordRenewal extends AppCompatActivity {
    private EditText pass1, pass2, answer;
    private TextView question;
    private Button change;
    FirebaseFirestore db;
    private String uID,email, oldpass;
    private FirebaseAuth nyauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_renewal);
        db = FirebaseFirestore.getInstance();
        question = findViewById(R.id.questionRecover);
        answer = findViewById(R.id.ansRecovery);
        change = findViewById(R.id.nextRecovery);
        pass1 = findViewById(R.id.etNewPass1);
        pass2 = findViewById(R.id.etNewPass2);
        nyauth=FirebaseAuth.getInstance();
        Bundle passing = getIntent().getExtras();
        uID = passing.getString("uID");
        email = passing.getString("email");
        setQuestionView();
        change.setOnClickListener(view -> {
            passwordReset();
        });

    }

    public void setQuestionView() {
        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot usr = task.getResult();
                String firebaseQ = usr.get("securityQuestion").toString();
                question.setText(firebaseQ);
            } else {
                question.setText("Null");
            }
        });
    }

    public void passwordReset() {

        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String sQuest = document.get("securityAnswer").toString();
                   oldpass = document.getString("Password");
                    if (check()) {
                        if (sQuest.equals(answer.getText().toString())) {
                            nyauth.signInWithEmailAndPassword(email,oldpass).addOnSuccessListener(authResult -> {
                               FirebaseUser user = nyauth.getCurrentUser();
                               user.updatePassword(pass2.getText().toString());
                                documentReference.update("Password", pass2.getText().toString());
                                Toast.makeText(this, "Contraseña recuperada con éxito", Toast.LENGTH_SHORT).show();
                                nyauth.signOut();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            });

                        } else {
                            answer.setError("Respuesta Incorrecta");
                            answer.requestFocus();
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Ups...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean check() {
        String newPass1 = pass1.getText().toString();
        String newPass2 = pass2.getText().toString();
        if (TextUtils.isEmpty(newPass1)) {
            pass1.setError("Coloca una nueva contraseña");
            pass1.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newPass2)) {
            pass2.setError("Confirma tu nueva contraseña");
            pass2.requestFocus();
            return false;
        }
        if (!TextUtils.equals(newPass1, newPass2)) {
            pass2.setError("Las contraseñas no coinciden");
            pass2.requestFocus();
            return false;
        }
        return true;
    }

}