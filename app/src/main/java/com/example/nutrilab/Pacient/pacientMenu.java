package com.example.nutrilab.Pacient;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.nutrilab.MainActivity;
import com.example.nutrilab.Nutricionist.nutriAppointments;
import com.example.nutrilab.Nutricionist.nutriConfigFragment;
import com.example.nutrilab.Nutricionist.nutriDietsFragment;
import com.example.nutrilab.Nutricionist.nutriPatientsFragment;
import com.example.nutrilab.Nutricionist.nutriProfileFragment;
import com.example.nutrilab.Nutricionist.nutriRecipesFragment;
import com.example.nutrilab.R;
import com.example.nutrilab.about;
import com.example.nutrilab.contactInfo;
import com.example.nutrilab.patientAboutNutriFragment;
import com.example.nutrilab.patientDietFragment;
import com.example.nutrilab.patientProgressFragment;
import com.example.nutrilab.patientRecipesFragment;
import com.example.nutrilab.patientSettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class pacientMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawPatient;
    NavigationView naviPatient;
    Toolbar toolbar;

    private FirebaseAuth nyauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_menu);
        nyauth = FirebaseAuth.getInstance();
        drawPatient = findViewById(R.id.patientDrawerLayout);
        naviPatient = findViewById(R.id.patientNavView);
        getSupportFragmentManager().beginTransaction().add(R.id.patientFrame,new patientDietFragment()).commit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_button);
        naviPatient.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                drawPatient.openDrawer(GravityCompat.START);
                return true;

            case R.id.contact:
                startActivity(new Intent(this, contactInfo.class));
                break;
            case R.id.about:
                Log.i("a","a");
                startActivity(new Intent(this, about.class));
                Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infomenu, menu);
        return true;
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

            case R.id.patientMenuDiet:
                ft.replace(R.id.patientFrame, new patientDietFragment()).commit();
                break;
            case R.id.patientMenuRecipes:
                ft.replace(R.id.patientFrame, new patientRecipesFragment()).commit();
                break;
            case R.id.patientMenuProgress:
                ft.replace(R.id.patientFrame, new patientProgressFragment()).commit();
                break;
            case R.id.patientMenuNutriProfile:
                ft.replace(R.id.patientFrame, new patientAboutNutriFragment()).commit();
                break;
            case R.id.patientMenuSettings:
                ft.replace(R.id.patientFrame, new patientSettingsFragment()).commit();
                break;

            case R.id.patientMenuLogout:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                nyauth.signOut();
                break;
        }
        drawPatient.closeDrawer(GravityCompat.START);
    }
}