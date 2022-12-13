package com.example.nutrilab.Nutricionist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.nutrilab.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DialogSchedule extends AppCompatDialogFragment {

    List<String> day = new ArrayList<>();
    int hour, minute;
    TextView selected, txtStart, txtEnd;
    AppCompatSpinner spinDay;
    DialogListener listener;

    public DialogSchedule(List<String> currentDayList){
        this.day = currentDayList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_schedule, null);



        builder.setView(view)
                .setTitle("Añade un horario")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createShift();
                    }
                });
         selected = view.findViewById(R.id.selectedDay);
         txtStart = view.findViewById(R.id.startHourDialog);
         txtEnd = view.findViewById(R.id.endHourDialog);
         spinDay = view.findViewById(R.id.spinnerDay);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,day);
        spinDay.setAdapter(arrayAdapter);
        spinDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Día")){

                }
                else{
                    selected.setText(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        txtStart.setOnClickListener(view1 -> {
             timePick(txtStart);
        });

        txtEnd.setOnClickListener(view1 -> {
            timePick(txtEnd);
        });

        return builder.create();
    }

    public void timePick(TextView tw) {
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("NewApi")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                tw.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        },hour,minute, true);
        timePickerDialog.show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyTexts(String day, String sHour, String eHour);
    }

    public void createShift(){
        String day = selected.getText().toString();
        String hourStart=txtStart.getText().toString();
        String hourEnd = txtEnd.getText().toString();
        if(day.equals("Día")){
            spinDay.requestFocus();
        }else if(hourStart.equals(hourEnd)){
            txtStart.requestFocus();
            txtEnd.requestFocus();
            txtStart.setError("Coloca horas diferentes");
        }
        else if(hourStart.equals("--:--")){
            txtStart.requestFocus();
            txtStart.setError("Elige una hora");
        }
        else if(hourEnd.equals("--:--")){
            txtEnd.requestFocus();
            txtEnd.setError("Elige una hora");
        }
        else if (!checkHour(hourStart,hourEnd)){

        }
        else{
            listener.applyTexts(day,hourStart,hourEnd);
        }

    }

    private boolean checkHour(String sHour, String eHour) {
        Date startHour = new Date();
        Date endHour = new Date();
        String[] StartParts = sHour.split(":");
        startHour.setHours(Integer.parseInt(StartParts[0]));
        startHour.setMinutes(Integer.parseInt(StartParts[1]));
        String[] EndParts = eHour.split(":");
        endHour.setHours(Integer.parseInt(EndParts[0]));
        endHour.setMinutes(Integer.parseInt(EndParts[1]));
        return startHour.before(endHour);
    }
}




