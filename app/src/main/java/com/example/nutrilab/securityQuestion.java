package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Pacient.pacientMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class securityQuestion extends AppCompatActivity {
    private TextView selected;
    private Spinner spinner;
    private EditText answer;
    private Button save;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        answer = findViewById(R.id.secret_answer_r);
        selected=findViewById(R.id.selectedQuestion);
        spinner = findViewById(R.id.spinnerQuestion);
        save = findViewById(R.id.btnSecurity);
        uID= nyauth.getUid();

        List<String> Questions = new ArrayList<>();
        Questions.add(0," Elige una pregunta ");
        Questions.add("¿Cuál fue el nombre de tu primera mascota?");
        Questions.add("¿Cuándo es el cumpleaños de tu mejor amigo?");
        Questions.add("¿Cuál fue tu primer trabajo?");
        Questions.add("¿En qué ciudad naciste?");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Questions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals(" Elige una pregunta ")){

                }
                else{
                    selected.setText(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(view -> {
            securityQuestion();
        });
    }

    public void securityQuestion(){
        String secretAnswer = answer.getText().toString();
        String question = spinner.getSelectedItem().toString();
        if(TextUtils.isEmpty(secretAnswer)){
            answer.setError("Responde la pregunta malditasea");
            answer.requestFocus();
        }
        else if(question.equals(" Elige una pregunta ")){
            Toast.makeText(securityQuestion.this, "Por favor elige una pregunta",Toast.LENGTH_SHORT).show();
        }
        else{
            DocumentReference documentReference = db.collection("users").document(uID);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document =task.getResult();
                    if(document.exists()){
                        documentReference.update("securityQuestion", question);
                        documentReference.update("securityAnswer", secretAnswer);
                        Toast.makeText(securityQuestion.this, "Pregunta de Seguridad registrada",Toast.LENGTH_SHORT).show();
                        int type = document.getLong("Type").intValue();
                        switch (type){
                            case 1:
                                nyauth.signOut();
                                Intent nutri = new Intent(securityQuestion.this, MainActivity.class);
                                startActivity(nutri);
                                finish();
                                break;
                            case 2:
                                Intent pacient = new Intent(securityQuestion.this, pacientMenu.class);
                                startActivity(pacient);
                                finish();
                                break;
                            case 3:
                                //WIP
                                break;
                            default:
                                Toast.makeText(this,"Algo salió terriblemente mal",Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }

                }
                else{
                    Toast.makeText(this,"Algo salió terriblemente mal",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}