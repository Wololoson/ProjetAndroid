package com.example.wilson.projet03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FeaturesActivity extends AppCompatActivity {
    //Déclaration des variables membres
    private Intent intent_recup;
    private String name, desc, str, def, hp, mp;
    private ImageView img;
    private TextView nameView, descView, strView, defView, hpView, mpView;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        //Initialisation des variables membres
        intent_recup = getIntent();

        name = intent_recup.getStringExtra("name");

        img = (ImageView)findViewById(R.id.img);

        nameView = (TextView)findViewById(R.id.name);
        descView = (TextView)findViewById(R.id.desc);
        strView = (TextView)findViewById(R.id.featuresStrength);
        defView = (TextView)findViewById(R.id.featuresDef);
        hpView = (TextView)findViewById(R.id.featureshp);
        mpView = (TextView)findViewById(R.id.featuresMp);

        backBtn = (Button)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(backList);

        fill();
    }

    //Retour à l'activité principale
    View.OnClickListener backList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //Remplissage des Views avec les données passées en paramètre
    public void fill(){
        if(name.compareTo(getString(R.string.knight)) == 0){
            img.setImageResource(R.drawable.knight);
            img.setContentDescription("knight image");
            desc = getString(R.string.knight_desc);
            str = getString(R.string.k_strength_p);
            def = getString(R.string.k_def_p);
            hp = getString(R.string.k_hp_p);
            mp = getString(R.string.k_mp_p);
        }
        else if(name.compareTo(getString(R.string.monk)) == 0){
            img.setImageResource(R.drawable.monk);
            img.setContentDescription("monk image");
            desc = getString(R.string.monk_desc);
            str = getString(R.string.m_strength_p);
            def = getString(R.string.m_def_p);
            hp = getString(R.string.m_hp_p);
            mp = getString(R.string.m_mp_p);
        }

        nameView.setText(name);
        descView.setText(desc);
        strView.setText(str);
        defView.setText(def);
        hpView.setText(hp);
        mpView.setText(mp);
    }
}
