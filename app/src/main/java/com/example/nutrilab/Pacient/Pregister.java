package com.example.nutrilab.Pacient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrilab.R;
import com.example.nutrilab.adminScreen;
import com.example.nutrilab.securityQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pregister extends AppCompatActivity {
    private EditText name, lname, email, pass, age, ced;
    private Button register;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private String userID;
    private DocumentReference nReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregister);
        name = findViewById(R.id.pacNameR);
        lname = findViewById(R.id.pacLnameR);
        email = findViewById(R.id.pacEmailR);
        pass = findViewById(R.id.pacPassR);
        age = findViewById(R.id.pacLage);
        ced = findViewById(R.id.pacCed);
        register = findViewById(R.id.pacButtonR);
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
        String flage = age.getText().toString();
        String fced = ced.getText().toString();

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
        }
        else if (TextUtils.isEmpty(flage)) {
            age.setError("Llena todos los campos");
            age.requestFocus();
        }
        else if (TextUtils.isEmpty(fced)) {
            ced.setError("Llena todos los campos");
            ced.requestFocus();
        }
        else {
            Query query = db.collection("users").whereEqualTo("nutriologoData.nCedula", fced);
            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        if(snapshotList.isEmpty()){
                            ced.setError("Este numero de cédula no existe");
                            ced.requestFocus();
                        }else{
                            for (DocumentSnapshot snapshot:snapshotList){
                                nReference=snapshot.getReference();
                            }
                            uploadPatient(fmail,fpassword,flage,fname,flname);
                        }
                    });
                }
            });
        }
    }


    public void uploadPatient(String fmail, String fpassword, String flage, String fname, String flname ){
        nyauth.createUserWithEmailAndPassword(fmail, fpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = nyauth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    Map<String,Object> PacientData = new HashMap<>();
                    PacientData.put("Edad", flage);
                    PacientData.put("Peso", 0.0);
                    PacientData.put("Cadera", 0.0);
                    PacientData.put("Cintura",0.0);
                    PacientData.put("Ombligo", 0.0);
                    PacientData.put("Brazo",0.0);
                    user.put("Nombre", fname);
                    user.put("Apellidos", flname);
                    user.put("Email", fmail);
                    user.put("Password", fpassword);
                    user.put("Type", 2);
                    user.put("NutriLinked", nReference);
                    user.put("Datos", PacientData);
                    Toast.makeText(Pregister.this, nReference.toString(), Toast.LENGTH_SHORT).show();
                    //nReference.update("linkedPatients", FieldValue.arrayUnion(documentReference));
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Pregister.this, "Paciente registrado con éxito", Toast.LENGTH_SHORT).show();
                            DocumentReference ref = db.collection("users").document(userID);
                            ref.update("Reference",ref);
                            Intent sq = new Intent(Pregister.this, securityQuestion.class);
                            startActivity(sq);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(Pregister.this, "Lo sentimos, no se pudo registrar el usuario" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}