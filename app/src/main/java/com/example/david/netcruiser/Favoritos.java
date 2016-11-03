package com.example.david.netcruiser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Favoritos extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa;
    BDNavegador bd;
    int favorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bd = new BDNavegador(this);
        lv = (ListView)findViewById(R.id.listView);
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, bd.consultarFavoritos());
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                favorito=posicion;
                lanzaFavorito();
            }
        });
    }

    public void lanzaFavorito(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("url", aa.getItem(favorito).toString());
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
