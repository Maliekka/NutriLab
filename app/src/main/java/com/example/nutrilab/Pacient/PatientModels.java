package com.example.nutrilab.Pacient;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.nutrilab.Nutricionist.NutriModels;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class PatientModels implements Parcelable{
    private String Nombre;
    private String Apellidos;
    private String Email;
    private DocumentReference NutriLinked;
    private DocumentReference Reference;
    private int Type;
    private HashMap<String,Object> Datos = new HashMap<>();

    private PatientModels(){
    }
   @Override
   public String toString(){
        return Nombre + " " + Apellidos;
   }


    protected PatientModels(Parcel in) {
        Nombre = in.readString();
        Apellidos = in.readString();
        Email = in.readString();
        NutriLinked = FirebaseFirestore.getInstance().document(in.readString());
        Type = in.readInt();
        Datos = (HashMap<String , Object>)in.readSerializable();
        Reference = FirebaseFirestore.getInstance().document(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Nombre);
        dest.writeString(Apellidos);
        dest.writeString(Email);
        dest.writeString(NutriLinked.getPath());
        dest.writeInt(Type);
        dest.writeSerializable(Datos);
        dest.writeString(Reference.getPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientModels> CREATOR = new Creator<PatientModels>() {
        @Override
        public PatientModels createFromParcel(Parcel in) {
            return new PatientModels(in);
        }

        @Override
        public PatientModels[] newArray(int size) {
            return new PatientModels[size];
        }
    };

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public DocumentReference getReference() {
        return Reference;
    }

    public void setReference(DocumentReference reference) {
        Reference = reference;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public DocumentReference getNutriLinked() {
        return NutriLinked;
    }

    public void setNutriLinked(DocumentReference nutriLinked) {
        NutriLinked = nutriLinked;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public HashMap<String, Object> getDatos() {
        return Datos;
    }

    public void setDatos(HashMap<String, Object> datos) {
        Datos = datos;
    }
}
