package com.example.wilson.projet03;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MenuPersoActivity extends AppCompatActivity {
    //Déclaration des variables membres
    private CheckBox chk_k, chk_m;
    private Button btn;
    private int str, def, hp, mp;
    private Intent intent;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_perso);

        //Initalisation des variables membres
        chk_k = (CheckBox)findViewById(R.id.checkBoxKnight);
        chk_m = (CheckBox)findViewById(R.id.checkBoxMonk);

        btn = (Button)findViewById(R.id.confirmButton);
        btn.setOnClickListener(listener_confirm);

        intent = new Intent(MenuPersoActivity.this, MazeActivity.class);
    }

    //Vérification de la classe choisie, gestion d'erreur et démarrage de l'activité principale
    View.OnClickListener listener_confirm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if((chk_k.isChecked() && chk_m.isChecked()) || (!chk_k.isChecked() && !chk_m.isChecked())){
                errorDisplay();
            }
            else{
                if(chk_k.isChecked()){
                    name = getString(R.string.knight);
                    str = Integer.parseInt(getString(R.string.k_strength_p));
                    def = Integer.parseInt(getString(R.string.k_def_p));
                    hp = Integer.parseInt(getString(R.string.k_hp_p));
                    mp = Integer.parseInt(getString(R.string.k_mp_p));
                }
                else if(chk_m.isChecked()){
                    name = getString(R.string.monk);
                    str = Integer.parseInt(getString(R.string.m_strength_p));
                    def = Integer.parseInt(getString(R.string.m_def_p));
                    hp = Integer.parseInt(getString(R.string.m_hp_p));
                    mp = Integer.parseInt(getString(R.string.m_mp_p));
                }

                intent.putExtra("name", name);
                intent.putExtra("str", str);
                intent.putExtra("def", def);
                intent.putExtra("hp", hp);
                intent.putExtra("mp", mp);

                startActivity(intent);
            }
        }
    };

    //Fonction d'affichage de l'erreur
    public void errorDisplay(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.error1);
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
