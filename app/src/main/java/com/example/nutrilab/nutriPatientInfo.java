package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.nutrilab.Pacient.PatientModels;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class nutriPatientInfo extends AppCompatActivity {
    private TextView nombre, edad, peso, cadera, cintura, brazo, ombligo;
    private Button editar, eliminar, derivar;
    private FirebaseFirestore db;
    private String document;
    private PatientModels model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_patient_info);
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
        model = intent.getParcelableExtra("dataPatient");

        editar.setOnClickListener(view -> {
            Intent i = new Intent(this, nutriEditPData.class);
            i.putExtra("dataPatient",model);
            startActivity(i);
        });


        setView();

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