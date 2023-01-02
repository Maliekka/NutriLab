package com.example.nutrilab;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Nutricionist.nutriMenu;
import com.example.nutrilab.Pacient.pacientMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Button reg, log;
    private FirebaseAuth nyauth;
    private FirebaseFirestore db;
    private String uID;
    private EditText mail, pass;
    private TextView forgot;
    private boolean passVisibility;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_NutriLab);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uID = nyauth.getUid();
        toolbar();
        mail = findViewById(R.id.logmail);
        pass = findViewById(R.id.logpass);
        reg = findViewById(R.id.register);
        log = findViewById(R.id.login);
        forgot = findViewById(R.id.forgotPass);
        log.setOnClickListener(view -> userLog());
        goToRegister();

        forgot.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, passwordRecovery.class));
        });

        pass.setOnTouchListener((view, motionEvent) -> {
            final int right = 2;
            if(motionEvent.getAction()== MotionEvent.ACTION_UP){
                if(motionEvent.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[right].getBounds().width()){
                    int selection = pass.getSelectionEnd();
                    if(passVisibility){
                        pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_eye,0);
                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passVisibility = false;
                    }
                    else{
                        pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_eyent,0);
                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passVisibility = true  ;
                    }
                    pass.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = nyauth.getCurrentUser();
        if (user != null) {
            DocumentReference documentReference = db.collection("users").document(uID);
            documentReference.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int type = document.getLong("Type").intValue();

                    switch (type) {
                        case 1:

                            checkNutriAccounts(document.getLong("statusAccount").intValue(),nyauth);
                            break;
                        case 2:
                            Intent pacient = new Intent(MainActivity.this, pacientMenu.class);
                            startActivity(pacient);
                            finish();
                            break;
                        case 3:
                            Intent admin = new Intent(MainActivity.this, adminScreen.class);
                            startActivity(admin);
                            finish();
                            break;
                        default:
                            Toast.makeText(this, "Algo salió terriblemente mal", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(this, "Algo salió terriblemente mal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void goToRegister() {
        reg.setOnClickListener(view -> {
            Intent toreg = new Intent(MainActivity.this, registerOptions.class);
            startActivity(toreg);
        });
    }

    public void userLog() {
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mail.setError("Introduce tu correo");
            mail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            pass.setError("Introduce tu contraseña");
            pass.requestFocus();
        } else {
            nyauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uID = nyauth.getUid();
                        DocumentReference documentReference = db.collection("users").document(uID);
                        documentReference.get().addOnCompleteListener(task1 -> {
                            DocumentSnapshot document = task1.getResult();
                            if (document.exists()) {
                                int type = document.getLong("Type").intValue();
                                switch (type) {
                                    case 1:
                                        checkNutriAccounts(document.getLong("statusAccount").intValue(),nyauth);
                                        break;
                                    case 2:
                                        Intent pacient = new Intent(MainActivity.this, pacientMenu.class);
                                        startActivity(pacient);
                                        finish();
                                        break;
                                    case 3:
                                        Intent admin = new Intent(MainActivity.this, adminScreen.class);
                                        startActivity(admin);
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "Algo salió terriblemente mal", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Algo salió terriblemente mal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                        Log.w("TAG", "ERROR", task.getException());
                    }
                }
            });
        }
    }

    public void checkNutriAccounts(int status, FirebaseAuth current) {
        AlertDialog.Builder login = new AlertDialog.Builder(this);
        login.setTitle("No se pudo iniciar sesión");
        login.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                current.signOut();
            }
        });

        switch (status) {
            case 0:

                login.setMessage("El número de cédula que proporcionaste no ha " +
                        "sido validado por un Administrador. Se tiene un máximo de" +
                        " 48 horas desde que se registró la cuenta para la validación del numero de" +
                        " cédula.");
                login.show();
                current.signOut();
                break;

            case 1:
                Intent nutri = new Intent(MainActivity.this, nutriMenu.class);
                startActivity(nutri);
                finish();
                break;
            case 2:

                login.setMessage("El número de cedula que proporcionaste no es " +
                        "válido. Por favor comunícate con un administrador si " +
                        "crees que se trata de un error.");
                login.show();
                break;
        }
    }
    public void toolbar(){
        Toolbar adminTB = findViewById(R.id.toolbar);
        setSupportActionBar(adminTB); //pasa a la posición que originalmente ocupaba el action bar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this, about.class));
                break;

            case R.id.contact:
                startActivity(new Intent(this, contactInfo.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}