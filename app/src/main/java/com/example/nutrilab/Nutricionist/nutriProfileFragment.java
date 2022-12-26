package com.example.nutrilab.Nutricionist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrilab.Horarios;
import com.example.nutrilab.R;
import com.example.nutrilab.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class nutriProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth nyauth;
    private ImageView profilePic;
    private TextView nombre,apellidos,ncedula,acercade, precio;
    private ImageButton toEdit;
    private String uID;
    private List<Shift> ListSchedule = new ArrayList<>();
    private LinearLayout scheduleLayout;


    public nutriProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutri_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        nyauth = FirebaseAuth.getInstance();
        nombre = (TextView) getView().findViewById(R.id.NutriProfileName);
        apellidos = (TextView) getView().findViewById(R.id.NutriProfileLastName);
        ncedula = (TextView) getView().findViewById(R.id.NutriProfileNced);
        precio = (TextView) getView().findViewById(R.id.NutriProfilePrice);
        acercade = (TextView) getView().findViewById(R.id.NutriProfileAbout);
        scheduleLayout = (LinearLayout) getView().findViewById(R.id.scheduleProfileNut);
        uID=nyauth.getUid();
        profilePic = (ImageView) getView().findViewById(R.id.nutPrPic);
        toEdit = (ImageButton) getView().findViewById(R.id.editNutriProfile);
        setView();
        toEdit.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), editNutriProfile.class));
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void applyTexts(String day, String sHour, String eHour) {
        //Shift turn = new Shift(day,sHour,eHour);
        View shift = getLayoutInflater().inflate(R.layout.row_profile_schedule,null,false);
        TextView dayTW = (TextView) shift.findViewById(R.id.SProfileDay);
        TextView startHourTW = (TextView) shift.findViewById(R.id.SProfileStart);
        TextView endHourTW = (TextView) shift.findViewById(R.id.SProfileEnd);

        dayTW.setText(day);
        startHourTW.setText(sHour);
        endHourTW.setText(eHour);
        scheduleLayout.addView(shift);


    }


    public void setView(){
        ListSchedule.clear();
        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot usr = task.getResult();
                //obtener datos en texto
                String name = usr.get("Nombre").toString();
                String lastName = usr.get("Apellidos").toString();
                String nCed = usr.get("nutriologoData.nCedula").toString();
                String about = usr.get("nutriologoData.About").toString();
                String price = usr.get("nutriologoData.Precio").toString();
                //obtener horarios
                ListSchedule = usr.toObject(Horarios.class).Horario;
                if(ListSchedule.size()!=0){
                    for (Shift s:ListSchedule) {
                        applyTexts(s.getDay(),s.getTimeStart(),s.getTimeEnd());
                    }
                }
                //obtener foto de perfil
                if(usr.get("profilePic").toString()!=null){
                    String pfp = usr.get("profilePic").toString();
                    Picasso.get().load(pfp).into(profilePic);
                    //profilePic.setImageResource(R.drawable.lorempicsum);
                }else {
                    profilePic.setImageResource(R.drawable.lorempicsum);
                }
                nombre.setText(name);
                apellidos.setText(lastName);
                ncedula.setText(nCed);
                acercade.setText(about);
                precio.setText(price);
            }
            else{
                profilePic.setImageResource(R.drawable.lorempicsum);
                nombre.setText("NaN");
                precio.setText("NaN");
                apellidos.setText("NaN");
                ncedula.setText("NaN");
                acercade.setText("NaN");
            }
        });
    }
}