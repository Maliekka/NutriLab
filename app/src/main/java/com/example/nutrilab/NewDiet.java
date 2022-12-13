package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Pacient.PatientModels;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NewDiet extends AppCompatActivity {
    EditText[] plan = new EditText[15];
    Integer[] nPlan = new Integer[15];
    Spinner SpinnerPatient;
    Button create;
    private FirebaseFirestore db;
    private FirebaseAuth nyauth;
    private String uID;
    PatientModels p;
    List<PatientModels> patients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet);
        plan[0] = findViewById(R.id.DesETProtein);
        plan[1] = findViewById(R.id.DesETVerd);
        plan[2] = findViewById(R.id.DesETFrijolitos);
        plan[3] = findViewById(R.id.DesETFruit);
        plan[4] = findViewById(R.id.DesETGrasas);

        plan[5] = findViewById(R.id.ComidaETProtein);
        plan[6] = findViewById(R.id.ComidaETVerd);
        plan[7] = findViewById(R.id.ComidaETFrijolitos);
        plan[8] = findViewById(R.id.ComidaETFruit);
        plan[9] = findViewById(R.id.ComidaETGrasas);

        plan[10] = findViewById(R.id.CenETProtein);
        plan[11] = findViewById(R.id.CenETVerd);
        plan[12] = findViewById(R.id.CenETFrijolitos);
        plan[13] = findViewById(R.id.CenETFruit);
        plan[14] = findViewById(R.id.CenETGrasas);

        SpinnerPatient = findViewById(R.id.spinnerPatient);

        db = FirebaseFirestore.getInstance();
        nyauth= FirebaseAuth.getInstance();
        uID=nyauth.getUid();

        DocumentReference reference  = db.collection("users").document(uID);

        Query q = db.collection("users").whereEqualTo("NutriLinked",reference);
        q.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot snapshot: snapshotList) {
                patients.add(snapshot.toObject(PatientModels.class));
            }

            ArrayAdapter<PatientModels> dataAdapter = new ArrayAdapter<PatientModels>(this, android.R.layout.simple_spinner_item,patients);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinnerPatient.setAdapter(dataAdapter);
            SpinnerPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    p = (PatientModels) SpinnerPatient.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            create = findViewById(R.id.createPlan);

            create.setOnClickListener(v -> {
                CreateDiet();
            });
        });


    }



    public void CreateDiet() {
        for (int i = 0; i < 15; i++) {
            if (plan[i].getText().toString().isEmpty()) {//revisa si está vació el campo de la porción
                nPlan[i] = 0;//si es así, automaticamente asigna un 0
            }
            else {
                nPlan[i]=Integer.parseInt(plan[i].getText().toString());
                //si no, solo recoge el dato escrito y lo convierte a un entero
            }
        }
        HashMap<String,Object> NutriPlan = new HashMap<>();

        HashMap<String,Integer> Desayuno = new HashMap<>();
        Desayuno.put("Proteina",nPlan[0]);
        Desayuno.put("Verdura", nPlan[1]);
        Desayuno.put("Leguminosas",nPlan[2]);
        Desayuno.put("Fruta",nPlan[3]);
        Desayuno.put("Grasas",nPlan[4]);

        HashMap<String,Object> Comida = new HashMap<>();
        Comida.put("Proteina",nPlan[5]);
        Comida.put("Verdura", nPlan[6]);
        Comida.put("Leguminosas",nPlan[7]);
        Comida.put("Fruta",nPlan[8]);
        Comida.put("Grasas",nPlan[9]);

        HashMap<String,Object> Cena = new HashMap<>();
        Cena.put("Proteina",nPlan[10]);
        Cena.put("Verdura", nPlan[11]);
        Cena.put("Leguminosas",nPlan[12]);
        Cena.put("Fruta",nPlan[13]);
        Cena.put("Grasas",nPlan[14]);

        NutriPlan.put("Paciente", p.getNombre() + " " + p.getApellidos());
        NutriPlan.put("RefPaciente",p.getReference());
        NutriPlan.put("RefNutri",p.getNutriLinked());
        NutriPlan.put("Fecha",new Date());
        NutriPlan.put("Desayuno",Desayuno);
        NutriPlan.put("Comida",Comida);
        NutriPlan.put("Cena",Cena);

        db.collection("NutriPlans").add(NutriPlan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String planId=documentReference.getId();
                documentReference.update("PlanReference",planId);
                Intent i = new Intent(NewDiet.this,NewDietSnacks.class);
                i.putExtra("ID",planId);
                startActivity(i);
            }
        });

        };


}


