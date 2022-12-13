package com.example.nutrilab.Nutricionist;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nutrilab.Horarios;
import com.example.nutrilab.R;
import com.example.nutrilab.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class editNutriProfile extends AppCompatActivity implements DialogSchedule.DialogListener {

    private ImageView profilePic;
    private ImageButton addPhoto, addSchedule;
    private Button saveChanges;
    private EditText aboutField;
    private LinearLayout scheduleLayout;
    final List<String> dayList = new ArrayList<>();
    FirebaseAuth nyauth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    ActivityResultLauncher <String> mTakePhoto;
    private Uri nutriPicURI;
    private String uID;
    private List<Shift> ListSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nutri_profile);
        nyauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        uID = nyauth.getUid();
        scheduleLayout = findViewById(R.id.scheduleLayout);
        profilePic = findViewById(R.id.editPicNutri);
        addPhoto = findViewById(R.id.addPhotoNutri);
        aboutField = findViewById(R.id.editNutriProfileAbout);
        saveChanges = findViewById(R.id.saveNutriProfChanges);
        addSchedule = findViewById(R.id.addSchedule);
        ListSchedule = new ArrayList<>();
        dayList.add("Lunes");
        dayList.add("Martes");
        dayList.add("Miércoles");
        dayList.add("Jueves");
        dayList.add("Viernes");
        dayList.add("Sábado");
        dayList.add("Domingo");


        setView();

        addSchedule.setOnClickListener(view -> {
            //addScheduleView();
            openDialog();
        });


        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null) {
                            profilePic.setImageURI(result);
                            nutriPicURI = result;
                        }
                    }
                }
        );

        addPhoto.setOnClickListener(view -> {
            pickPhoto();
        });

        saveChanges.setOnClickListener(view -> {
            saveChanges();
            startActivity(new Intent(this,nutriMenu.class));
        });

    }

    public void setView(){
        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot usr = task.getResult();

                    ListSchedule = usr.toObject(Horarios.class).Horario;
                    if(ListSchedule.size()!=0){
                        for (Shift s:ListSchedule) {
                            applyTexts(s.getDay(),s.getTimeStart(),s.getTimeEnd());
                        }
                    }

                if(usr.get("profilePic").toString()!=null){
                    String pfp = usr.get("profilePic").toString();
                    Picasso.get().load(pfp).into(profilePic);
                }else {
                    profilePic.setImageResource(R.drawable.lorempicsum);
                }
            }
            else{
                profilePic.setImageResource(R.drawable.lorempicsum);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mTakePhoto.launch("image/*");
        }else{
            Toast.makeText(editNutriProfile.this, "Permiso denegado.", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveChanges(){
        if (ListSchedule != null) {
            ListSchedule.clear();
        }
        String aboutEdited = aboutField.getText().toString();
        View rowShift;
        for (int i = 0; i<scheduleLayout.getChildCount();i++){
           rowShift=scheduleLayout.getChildAt(i);
           TextView dayTV = rowShift.findViewById(R.id.dynamic_text_day);
           TextView startTV = rowShift.findViewById(R.id.dynamic_text_startHour);
           TextView endTV = rowShift.findViewById(R.id.dynamic_text_endHour);
           ListSchedule.add(new Shift(dayTV.getText().toString(), startTV.getText().toString(), endTV.getText().toString()));
        }

        String uID = nyauth.getUid();
        DocumentReference documentReference = db.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document =task.getResult();
                if(document.exists()){
                    documentReference.update("nutriologoData.About", aboutEdited);
                    documentReference.update("Horario",ListSchedule);


                    if(nutriPicURI!= null){
                        ContentResolver contentResolver = getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        StorageReference ref = storage.getReference().child("profilePics/img"+ UUID.randomUUID().toString()+"."+mime.getExtensionFromMimeType(contentResolver.getType(nutriPicURI)));
                        ref.putFile(nutriPicURI).addOnSuccessListener(taskSnapshot -> {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri = uriTask.getResult();
                            documentReference.update("profilePic",uri.toString());
                        });
                    }
                }
            }
        });

    }

    public void pickPhoto(){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("a", "onCreate: nono");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        }else {
            mTakePhoto.launch("image/*");
        }
    }

    public void openDialog(){
        DialogSchedule dialogSchedule = new DialogSchedule(dayList);
        dialogSchedule.show(getSupportFragmentManager(),"Horario");
    }

    @Override
    public void applyTexts(String day, String sHour, String eHour) {
        //Shift turn = new Shift(day,sHour,eHour);
        View shift = getLayoutInflater().inflate(R.layout.row_schedule,null,false);
        TextView dayTW = (TextView) shift.findViewById(R.id.dynamic_text_day);
        TextView startHourTW = (TextView) shift.findViewById(R.id.dynamic_text_startHour);
        TextView endHourTW = (TextView) shift.findViewById(R.id.dynamic_text_endHour);
        ImageButton delete = (ImageButton) shift.findViewById(R.id.dynamic_delete);
        dayList.remove(day);
        dayTW.setText(day);
        startHourTW.setText(sHour);
        endHourTW.setText(eHour);
        scheduleLayout.addView(shift);


        delete.setOnClickListener(view -> {
            dayList.add(day);
            scheduleLayout.removeView(shift);
            ListSchedule.remove(new Shift(day,sHour,eHour));
        });

    }
}