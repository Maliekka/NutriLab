package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nutrilab.Nutricionist.Nregister;
import com.example.nutrilab.Pacient.Pregister;

public class registerOptions extends AppCompatActivity {
    Button gotoN, gotoP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_options);
        gotoN = findViewById(R.id.nutOp);
        gotoP = findViewById(R.id.pacOp);

        gotoN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regN = new Intent(registerOptions.this, Nregister.class);
                startActivity(regN);
            }
        });

        gotoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regP = new Intent(registerOptions.this, Pregister.class);
                startActivity(regP);
            }
        });
    }
}