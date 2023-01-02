package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.nutrilab.Nutricionist.nutriMenu;
import com.example.nutrilab.Others.PushNotificationSender;
import com.example.nutrilab.Pacient.PatientModels;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class nutriPatientInfo extends AppCompatActivity {
    private TextView nombre, edad, peso, cadera, cintura, brazo, ombligo;
    private Button editar, eliminar, derivar;
    private FirebaseFirestore db;
    private FirebaseAuth nyauth;
    private String document;
    PatientModels model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_patient_info);
        nyauth = FirebaseAuth.getInstance();
        nombre=findViewById(R.id.PDataName);
        edad = findViewById(R.id.PDataAge);
        peso = findViewById(R.id.PDataPeso);
        cadera = findViewById(R.id.PDataCadera);
        cintura = findViewById(R.id.PDataCintura);
        brazo = findViewById(R.id.PDataBrazo);
        ombligo =findViewById(R.id.PDataOmbligo);
        editar = findViewById(R.id.editPData);
        eliminar = findViewById(R.id.elimPData);
        derivar = findViewById(R.id.derivarPData);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        model = intent.getParcelableExtra("dP");
        setView();

        editar.setOnClickListener(view -> {
            Intent i = new Intent(this, nutriEditPData.class);
            i.putExtra("dataPatient",model);
            startActivity(i);
        });

        eliminar.setOnClickListener(v -> {
            eliminarPaciente();
        });


    }

    public void eliminarPaciente(){
        DocumentReference patientReference = model.getReference();
        patientReference.update("NutriLinked", null).addOnSuccessListener(unused -> {
            onEliminationNotification();
            Intent i = new Intent(this, nutriMenu.class);
            startActivity(i);
            finish();
        });
    }

    public void onEliminationNotification(){
        String FCMToken = model.getFCMToken();
        if(FCMToken != null){
            PushNotificationSender.pushNotif(this, FCMToken,"Has sido dado de baja", "Tu nutriólogo te ha dado de baja.");
        }
    }

    public void setView(){
        nombre.setText(model.getNombre() + " " + model.getApellidos());
        HashMap<String,Object> datos = model.getDatos();
        edad.setText(datos.get("Edad").toString() + " años");
        peso.setText(datos.get("Peso").toString() + " kg");
        cadera.setText(datos.get("Cadera").toString() + " cm");
        cintura.setText(datos.get("Cintura").toString()+ " cm");
        brazo.setText(datos.get("Brazo").toString()+ " cm");
        ombligo.setText(datos.get("Ombligo").toString()+ " cm");
    }
}