package com.example.nutrilab.Nutricionist;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.HashMap;


public class NutriModels implements Parcelable {
    private String Nombre;
    private String Apellidos;
    private String Email;
    private Date regTimestamp;


    private NutriModels(){

    }

    public NutriModels(String Nombre, String Apellidos, String Email, Date regTimestamp, HashMap<String, Object> nutriologoData) {
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Email = Email;
        this.regTimestamp = regTimestamp;
        this.nutriologoData = nutriologoData;
    }

    protected NutriModels(Parcel in) {
        Nombre = in.readString();
        Apellidos = in.readString();
        Email = in.readString();
        nutriologoData = (HashMap<String , Object>)in.readSerializable();
        regTimestamp = (Date)in.readSerializable();
    }

    public static final Creator<NutriModels> CREATOR = new Creator<NutriModels>() {
        @Override
        public NutriModels createFromParcel(Parcel in) {
            return new NutriModels(in);
        }

        @Override
        public NutriModels[] newArray(int size) {
            return new NutriModels[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Nombre);
        parcel.writeString(Apellidos);
        parcel.writeString(Email);
        parcel.writeSerializable(nutriologoData);
        parcel.writeSerializable(regTimestamp);
    }

    public HashMap<String, Object> getNutriologoData() {
        return nutriologoData;
    }

    public void setNutriologoData(HashMap<String, Object> nutriologoData) {
        this.nutriologoData = nutriologoData;
    }

    private HashMap<String,Object> nutriologoData = new HashMap<>();


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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getRegTimestamp() {
        return regTimestamp;
    }

    public void setRegTimestamp(Date regTimestamp) {
        this.regTimestamp = regTimestamp;
    }



}
