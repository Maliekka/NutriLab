package com.example.nutrilab.Nutricionist;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.nutrilab.MainActivity;
import com.example.nutrilab.R;
import com.example.nutrilab.about;
import com.example.nutrilab.contactInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class nutriMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseFirestore db;
    String uID;
    DrawerLayout drawNutri;
    NavigationView naviNutri;
    Toolbar toolbar;
    private FirebaseAuth nyauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_menu);
        drawNutri= findViewById(R.id.nutriDrawerLayout);
        naviNutri = findViewById(R.id.nutriNavView);
        db = FirebaseFirestore.getInstance();
        nyauth = FirebaseAuth.getInstance();
        uID= nyauth.getUid();
        // toolbar and menu
        getSupportFragmentManager().beginTransaction().add(R.id.content,new nutriPatientsFragment()).commit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_button);
        naviNutri.setNavigationItemSelectedListener(this);

        GetToken();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                drawNutri.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (item.getItemId()){
            case R.id.about:
                Log.i("a","a");
                startActivity(new Intent(this, about.class));
                Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nutriMenuProfile:
                ft.replace(R.id.content, new nutriProfileFragment()).commit();

                break;

            case R.id.contact:
                startActivity(new Intent(this, contactInfo.class));
                break;

            case R.id.nutriMenuPacients:
                ft.replace(R.id.content, new nutriPatientsFragment()).commit();
                break;
            case R.id.nutriMenuAppointments:
                ft.replace(R.id.content, new nutriAppointments()).commit();
                break;
            case R.id.nutriMenuRecipes:
                ft.replace(R.id.content, new nutriRecipesFragment()).commit();
                break;
            case R.id.nutriMenuDiets:
                ft.replace(R.id.content, new nutriDietsFragment()).commit();
                break;
            case R.id.nutriMenuConfig:
                ft.replace(R.id.content, new nutriConfigFragment()).commit();
                break;
            case R.id.nutriMenuLogout:

                DocumentReference documentReference = db.collection("users").document(uID);
                documentReference.update("FCMToken", null).addOnSuccessListener(unused -> {
                    nyauth.signOut();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
                break;
        }
        drawNutri.closeDrawer(GravityCompat.START);
    }


    private void GetToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.update("FCMToken", token);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infomenu, menu);
        return true;
    }

}