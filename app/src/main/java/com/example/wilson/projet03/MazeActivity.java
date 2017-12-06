package com.example.wilson.projet03;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class MazeActivity extends AppCompatActivity {
    //Déclaration des variables membres
    ImageView tmpImg;
    TextView tmpTxt, nbSkill;
    TableRow tmpTr;
    TableRow.LayoutParams trParam1;
    TableRow.LayoutParams trParam2;
    TableLayout maze;
    TableLayout features;
    int[][] mazeTab;
    ImageView up, right, down, left;
    int x, y, h, w;
    int imgPerso;
    Button potionsBtn, potionForceBtn, potionRougeBtn, potionBleueBtn, plusBtn;
    int[] nbPopos;
    int str_p, def_p, hp_p, mp_p;
    String featuresTab [][];
    Intent intent_recup, intent_features;
    String name;
    Random rand;
    int nbPerles, nbPerlesInv, idImgPerso;
    int[][] pearlPosTab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        //Initalisation des variables membres
        rand = new Random();

        intent_recup = getIntent();

        maze = (TableLayout)findViewById(R.id.maze);
        features = (TableLayout) findViewById(R.id.features);

        mazeTab = new int[][] {
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3},
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3},
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3},
                {3,3,3,3,1,1,1,1,2,2,3,3,3,3},
                {3,3,3,3,1,2,2,1,2,2,3,3,3,3},
                {3,3,3,3,1,2,2,1,1,1,3,3,3,3},
                {3,3,3,3,1,1,1,1,2,1,3,3,3,3},
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3},
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3},
                {3,3,3,3,3,3,3,3,3,3,3,3,3,3}
        };

        name = intent_recup.getStringExtra("name");
        trParam1 = new TableRow.LayoutParams();
        trParam2 = new TableRow.LayoutParams();

        nbSkill = (TextView)findViewById(R.id.skillPoints);

        nbPopos = new int[]{2, 2, 2};

        if(savedInstanceState != null){ //Si ce n'est pas le premier appel de onCreate()
            idImgPerso = savedInstanceState.getInt("idImgPerso");

            str_p = savedInstanceState.getInt("str_p");
            def_p = savedInstanceState.getInt("def_p");
            hp_p = savedInstanceState.getInt("hp_p");
            mp_p = savedInstanceState.getInt("mp_p");

            x = savedInstanceState.getInt("x");
            y = savedInstanceState.getInt("y");
            w = savedInstanceState.getInt("w");
            h = savedInstanceState.getInt("h");

            nbPopos[0] = savedInstanceState.getInt("nbPopo1");
            nbPopos[1] = savedInstanceState.getInt("nbPopo2");
            nbPopos[2] = savedInstanceState.getInt("nbPopo3");

            nbPerlesInv = savedInstanceState.getInt("nbPerlesInv");
            nbPerles = savedInstanceState.getInt("nbPerles");
            pearlPosTab = new int[nbPerles][2];

            for(int i = 0; i < nbPerles; i++){
                pearlPosTab[i][0] = savedInstanceState.getInt("pearlPosI"+i);
                pearlPosTab[i][1] = savedInstanceState.getInt("pearlPosJ"+i);
            }

            putPearls();
        }
        else{ //Lors du premier appel de onCreate()
            str_p = intent_recup.getIntExtra("str", 0);
            def_p = intent_recup.getIntExtra("def", 0);
            hp_p = intent_recup.getIntExtra("hp", 0);
            mp_p = intent_recup.getIntExtra("mp", 0);

            x = 5;
            y = 7;
            w = 2;
            h = 3;

            nbPerlesInv = 0;
            nbPerles = rand.nextInt(5 - 3) + 3;
            pearlPosTab = new int[nbPerles][2];

            idImgPerso = R.drawable.link_bas01;

            putFirstPearls();
        }

        imgPerso = idImgPerso;

        generate();

        refreshFeatures();

        up = (ImageView)findViewById(R.id.up);
        up.setOnClickListener(listener_move);

        right = (ImageView)findViewById(R.id.right);
        right.setOnClickListener(listener_move);

        left = (ImageView)findViewById(R.id.left);
        left.setOnClickListener(listener_move);

        down = (ImageView)findViewById(R.id.down);
        down.setOnClickListener(listener_move);

        potionsBtn = (Button)findViewById(R.id.potions);
        potionsBtn.setOnClickListener(listener_activate);

        potionForceBtn = (Button)findViewById(R.id.strengthPotion);
        potionForceBtn.setOnClickListener(listener_popo);
        potionForceBtn.setVisibility(View.GONE);
        potionForceBtn.setText("Potion de force (reste "+nbPopos[0]+")");

        potionRougeBtn = (Button)findViewById(R.id.redPotion);
        potionRougeBtn.setOnClickListener(listener_popo);
        potionRougeBtn.setVisibility(View.GONE);
        potionRougeBtn.setText("Potion rouge (reste "+nbPopos[1]+")");

        potionBleueBtn = (Button)findViewById(R.id.bluePotion);
        potionBleueBtn.setOnClickListener(listener_popo);
        potionBleueBtn.setVisibility(View.GONE);
        potionBleueBtn.setText("Potion bleue (reste "+nbPopos[2]+")");

        intent_features = new Intent(MazeActivity.this, FeaturesActivity.class);
    }

    //Génaration aléatoire de l'emplacement des perles
    public void putFirstPearls(){
        int inc = 0;
        while(inc < nbPerles){
            for (int i = w; i < w+6; i++){
                for (int j = h; j < h+8; j++){
                    if(mazeTab[i][j] == 1 && inc < nbPerles){
                        if(i != x || j != y){
                            if(rand.nextInt(100) > 90){
                                mazeTab[i][j] = 4;
                                pearlPosTab[inc][0] = i;
                                pearlPosTab[inc][1] = j;
                                inc++;
                            }
                        }
                    }
                }
            }
        }
    }

    //Réaffichage des perles aux positions sauvegardées
    public void putPearls(){
        for(int i = 0; i < nbPerles; i++){
            if(pearlPosTab[i][0] != -1 || pearlPosTab[i][1] != -1)
                mazeTab[pearlPosTab[i][0]][pearlPosTab[i][1]] = 4;
        }
    }

    //Affichage et actualisation des caractéristiques du personnage
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void refreshFeatures(){
        featuresTab = new String[][] {
                {getString(R.string.strength),Integer.toString(str_p)},
                {getString(R.string.def),Integer.toString(def_p)},
                {getString(R.string.hp),Integer.toString(hp_p)},
                {getString(R.string.mp),Integer.toString(mp_p)}
        };
        features.removeAllViews();
        for (int i = 0; i < 4; i++){
            tmpTr = new TableRow(this);
            features.addView(tmpTr);
            for (int j = 0; j < 2; j++){
                tmpTxt = new TextView(this);
                switch (j){
                    case 0:
                        tmpTxt.setTextAppearance(R.style.basicText3);
                        break;
                    case 1:
                        tmpTxt.setTextAppearance(R.style.featuresValues);
                        break;
                }
                trParam2.column = i;
                tmpTxt.setText(featuresTab[i][j]);
                trParam2.width = ActionBar.LayoutParams.WRAP_CONTENT;
                trParam2.height = ActionBar.LayoutParams.WRAP_CONTENT;
                tmpTxt.setLayoutParams(trParam2);
                tmpTr.addView(tmpTxt);
            }
            //Création et actualisation des boutons d'ajout de compétence
            plusBtn = new Button(this);
            plusBtn.setText("+");
            plusBtn.setEnabled(false);
            plusBtn.setMinimumWidth(10);
            plusBtn.setWidth(150);
            plusBtn.setOnClickListener(listener_addSkill);
            switch (i){
                case 0:
                    plusBtn.setId(R.id.addStr);
                    break;
                case 1:
                    plusBtn.setId(R.id.addDef);
                    break;
                case 2:
                    plusBtn.setId(R.id.addHp);
                    break;
                case 3:
                    plusBtn.setId(R.id.addMp);
                    break;
            }
            tmpTr.addView(plusBtn);
            if(nbPerlesInv >= 3 && nbPerlesInv != 0){
                plusBtn.setEnabled(true);
            }
        }
        //Affichage des points de compétence
        if(nbPerlesInv%3 == 0 && nbPerlesInv >= 3)
            nbSkill.setText(Integer.toString((int)Math.floor((double)nbPerlesInv/3)));
        else if(nbPerlesInv < 3)
            nbSkill.setText(Integer.toString(0));
    }

    //Ajout d'un point dans les caractéristiques
    View.OnClickListener listener_addSkill = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.addStr:
                    str_p++;
                    break;
                case R.id.addDef:
                    def_p++;
                    break;
                case R.id.addHp:
                    hp_p++;
                    break;
                case R.id.addMp:
                    mp_p++;
                    break;
            }
            nbPerlesInv -= 3;
            refreshFeatures();
        }
    };

    //Affichage des boutons de potions
    View.OnClickListener listener_activate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setVisibility(View.GONE);
            potionForceBtn.setVisibility(View.VISIBLE);
            potionRougeBtn.setVisibility(View.VISIBLE);
            potionBleueBtn.setVisibility(View.VISIBLE);
        }
    };

    //Intéraction avec chaque bouton de potions
    View.OnClickListener listener_popo = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.strengthPotion:
                    if(nbPopos[0] > 0){
                        str_p++;
                        nbPopos[0]--;
                        potionForceBtn.setText("Potion de force (reste "+nbPopos[0]+")");
                    }
                    break;
                case R.id.redPotion:
                    if(nbPopos[1] > 0) {
                        hp_p += 10;
                        nbPopos[1]--;
                        potionRougeBtn.setText("Potion rouge (reste "+nbPopos[1]+")");
                    }
                    break;
                case R.id.bluePotion:
                    if(nbPopos[2] > 0) {
                        mp_p += 10;
                        nbPopos[2]--;
                        potionBleueBtn.setText("Potion bleue (reste "+nbPopos[2]+")");
                    }
                    break;
            }
            refreshFeatures();
        }
    };

    //Movement et orientation du personnage du personnage et ramassage des perles
    private View.OnClickListener listener_move = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.down:
                    if(mazeTab[x+1][y] == 1){
                        x++;
                        w++;
                        idImgPerso = R.drawable.link_bas01;
                    }
                    else if(mazeTab[x+1][y] == 4){
                        x++;
                        w++;
                        idImgPerso = R.drawable.link_bas01;
                        mazeTab[x][y] = 1;
                        nbPerlesInv++;
                        removePearl();
                    }
                    break;
                case R.id.right:
                    if(mazeTab[x][y+1] == 1){
                        y++;
                        h++;
                        idImgPerso = R.drawable.link_droite01;
                    }
                    else if(mazeTab[x][y+1] == 4){
                        y++;
                        h++;
                        idImgPerso = R.drawable.link_droite01;
                        mazeTab[x][y] = 1;
                        nbPerlesInv++;
                        removePearl();
                    }
                    break;
                case R.id.up:
                    if(mazeTab[x-1][y] == 1){
                        x--;
                        w--;
                        idImgPerso = R.drawable.link_haut01;
                    }
                    else if(mazeTab[x-1][y] == 4){
                        x--;
                        w--;
                        idImgPerso = R.drawable.link_haut01;
                        mazeTab[x][y] = 1;
                        nbPerlesInv++;
                        removePearl();
                    }
                    break;
                case R.id.left:
                    if(mazeTab[x][y-1] == 1){
                        y--;
                        h--;
                        idImgPerso = R.drawable.link_gauche01;
                    }
                    else if(mazeTab[x][y-1] == 4){
                        y--;
                        h--;
                        idImgPerso = R.drawable.link_gauche01;
                        mazeTab[x][y] = 1;
                        nbPerlesInv++;
                        removePearl();
                    }
                    break;
            }
            imgPerso = idImgPerso;
            maze.removeAllViews();
            refreshFeatures();
            generate();
        }
    };

    //Suppression d'une perle dans le tableau de positions des perles
    public void removePearl(){
        for(int i = 0; i < nbPerles; i++){
            if(pearlPosTab[i][0] == x && pearlPosTab[i][1] == y){
                pearlPosTab[i][0] = -1;
                pearlPosTab[i][1] = -1;
            }
        }
    }

    //Lancement de l'activité d'affichage des caractéristiques de base du personnahe
    View.OnClickListener featuresList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent_features.putExtra("name", name);
            startActivity(intent_features);
        }
    };

    //Génération et actualisation du labyrinthe
    public void generate(){
        for (int i = w; i < w+6; i++){
            tmpTr = new TableRow(this);
            maze.addView(tmpTr);
            for (int j = h; j < h+8; j++){
                tmpImg = new ImageView(this);
                trParam1.column = i;
                switch(mazeTab[i][j]){
                    case 1:
                        if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            if(i == x && j == y) {
                                tmpImg.setImageResource(imgPerso);
                                tmpImg.setClickable(true);
                                tmpImg.setOnClickListener(featuresList);
                            }
                            else
                                tmpImg.setImageResource(R.drawable.tile_chemin_xl);
                        }else{
                            if(i == x && j == y){
                                tmpImg.setImageResource(imgPerso);
                                tmpImg.setClickable(true);
                                tmpImg.setOnClickListener(featuresList);
                            }
                            else{
                                tmpImg.setImageResource(R.drawable.tile_chemin);
                            }
                        }
                        break;
                    case 2:
                        if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            tmpImg.setImageResource(R.drawable.tile_arbre_xl);

                        }else{
                            tmpImg.setImageResource(R.drawable.tile_arbre);
                        }
                        break;
                    case 3:
                        if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            tmpImg.setImageResource(R.drawable.tile_rocher_xl);

                        }else{
                            tmpImg.setImageResource(R.drawable.tile_rocher);
                        }
                        break;
                    case 4:
                        if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            if(i == x && j == y) {
                                tmpImg.setImageResource(imgPerso);
                                tmpImg.setClickable(true);
                                tmpImg.setOnClickListener(featuresList);
                            }
                            else
                                tmpImg.setImageResource(R.drawable.pearl_tile_xl);

                        }else{
                            if(i == x && j == y) {
                                tmpImg.setImageResource(imgPerso);
                                tmpImg.setClickable(true);
                                tmpImg.setOnClickListener(featuresList);
                            }
                            else
                                tmpImg.setImageResource(R.drawable.pearl_tile);
                        }
                        break;
                }
                tmpImg.setLayoutParams(trParam1);
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_LARGE){
                    tmpImg.getLayoutParams().height = 65;
                    tmpImg.getLayoutParams().width = 65;
                }
                else{
                    tmpImg.getLayoutParams().height = 120;
                    tmpImg.getLayoutParams().width = 120;
                }
                tmpTr.addView(tmpImg);
            }
        }
    }

    //Sauvegarde des données qui doivent l'être
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt("idImgPerso", idImgPerso);

        saveInstanceState.putInt("x", x);
        saveInstanceState.putInt("y", y);
        saveInstanceState.putInt("w", w);
        saveInstanceState.putInt("h", h);

        saveInstanceState.putInt("str_p", str_p);
        saveInstanceState.putInt("def_p", def_p);
        saveInstanceState.putInt("hp_p", hp_p);
        saveInstanceState.putInt("mp_p", mp_p);

        saveInstanceState.putInt("nbPopo1", nbPopos[0]);
        saveInstanceState.putInt("nbPopo2", nbPopos[1]);
        saveInstanceState.putInt("nbPopo3", nbPopos[2]);

        saveInstanceState.putInt("nbPerlesInv", nbPerlesInv);
        saveInstanceState.putInt("nbPerles", nbPerles);

        for(int i = 0; i < nbPerles; i++){
            saveInstanceState.putInt("pearlPosI"+i, pearlPosTab[i][0]);
            saveInstanceState.putInt("pearlPosJ"+i, pearlPosTab[i][1]);
        }
    }
}
