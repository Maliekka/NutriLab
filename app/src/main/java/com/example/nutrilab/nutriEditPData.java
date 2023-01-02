package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutrilab.Pacient.PatientModels;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class nutriEditPData extends AppCompatActivity {
    TextView nombre;
    EditText edad, peso, brazo, cadera, cintura, ombligo;
    Button save, cancel;
    PatientModels model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_edit_pdata);
        nombre = findViewById(R.id.PEditDataName);
        edad = findViewById(R.id.PEditDataAge);
        peso = findViewById(R.id.PEditDataPeso);
        cadera = findViewById(R.id.PEditDataCadera);
        brazo = findViewById(R.id.PEditDataBrazo);
        cintura = findViewById(R.id.PEditDataCintura);
        ombligo = findViewById(R.id.PEditDataOmbligo);
        save = findViewById(R.id.savePData);
        cancel = findViewById(R.id.cancelPData);
        Intent intent = getIntent();
        model = intent.getParcelableExtra("dataPatient");
        setView();

        save.setOnClickListener(view -> {
            saveChanges();
        });

        cancel.setOnClickListener(v -> {
            finish();
        });

    }

    private void setView() {
        nombre.setText(model.getNombre() + " " + model.getApellidos());
        HashMap<String,Object> datos = model.getDatos();
        edad.setText(datos.get("Edad").toString());
        peso.setText(datos.get("Peso").toString());
        cadera.setText(datos.get("Cadera").toString());
        cintura.setText(datos.get("Cintura").toString());
        brazo.setText(datos.get("Brazo").toString());
        ombligo.setText(datos.get("Ombligo").toString());
    }

    private void saveChanges(){
        HashMap<String,Object> d = new HashMap<>();
        String TextoEdad = edad.getText().toString();
        if(TextoEdad.isEmpty())
            TextoEdad="0";
        double TextoPeso, TextoCadera, TextoCintura, TextoBrazo, TextoOmbligo;
        if(!peso.getText().toString().isEmpty())
           TextoPeso = Double.parseDouble(peso.getText().toString());
        else
            TextoPeso = 0.0;

        if(!cadera.getText().toString().isEmpty())
            TextoCadera = Double.parseDouble(cadera.getText().toString());
        else
            TextoCadera = 0.0;

        if(!cintura.getText().toString().isEmpty())
            TextoCintura = Double.parseDouble(cintura.getText().toString());
        else
            TextoCintura = 0.0;

        if(!brazo.getText().toString().isEmpty())
            TextoBrazo = Double.parseDouble(brazo.getText().toString());
        else
            TextoBrazo = 0.0;

        if(!ombligo.getText().toString().isEmpty())
            TextoOmbligo = Double.parseDouble(ombligo.getText().toString());
        else
            TextoOmbligo = 0.0;
        d.put("Edad",TextoEdad);
        d.put("Peso",TextoPeso);
        d.put("Cintura", TextoCintura);
        d.put("Cadera", TextoCadera);
        d.put("Brazo", TextoBrazo);
        d.put("Ombligo", TextoOmbligo);
        DocumentReference ref = model.getReference();
        model.setDatos(d);
        ref.update("Datos", d);
        Intent i = new Intent(this, nutriPatientInfo.class);
        i.putExtra("dP",model);
        startActivity(i);
        finish();
    }
}