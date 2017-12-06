package com.example.wilson.projet03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AcceuilActivity extends AppCompatActivity {
    //Déclaration des variables membres
    private Intent intent;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        //Initalisation des variables membres
        intent = new Intent(AcceuilActivity.this, MenuPersoActivity.class);

        btn = (Button)findViewById(R.id.playBtn);
        btn.setOnClickListener(play);
    }

    //Lancer l'écran de sélection du personnage
    View.OnClickListener play = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(intent);
        }
    };
}
