package com.example.nutrilab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

public class contactInfo extends AppCompatActivity {

    private ImageButton contactTw,contactMail,contactWhats;
    private String urlTwitter= "https://twitter.com/maliekkat";
    private String urlWhatsapp ="https://wa.me/3322524568";
    private String urlMail="mailto:ittzel.ramirez03.19@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        setContentView(R.layout.activity_contact_info);
        contactTw=findViewById(R.id.twitter);
        contactTw.setOnClickListener(view -> {
            Uri link = Uri.parse(urlTwitter);
            Intent league1 = new Intent(Intent.ACTION_VIEW,link);
            startActivity(league1);
        });

        contactMail = findViewById(R.id.contactEmail);
        contactMail.setOnClickListener(view -> {
            Uri link = Uri.parse(urlMail);
            Intent league2 = new Intent(Intent.ACTION_VIEW,link);
            startActivity(league2);
        });

        contactWhats = findViewById(R.id.whatsapp);
        contactWhats.setOnClickListener(view -> {
            Uri link = Uri.parse(urlWhatsapp);
            Intent league = new Intent(Intent.ACTION_VIEW,link);
            startActivity(league);
        });
    }

}