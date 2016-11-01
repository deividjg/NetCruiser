package com.example.david.netcruiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private WebView wv;
    private AutoCompleteTextView autocompleteTV;
    private BDNavegador bd;
    private String[] historial;
    private ArrayAdapter aa;
    private ProgressBar pb;
    private String favorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        autocompleteTV = (AutoCompleteTextView) findViewById(R.id.autocompleteTV);
        wv = (WebView)findViewById(R.id.wv);
        bd = new BDNavegador(this); //también puedo poner getApplicationContext().
        pb = (ProgressBar)findViewById(R.id.progressBar);
        favorito = null;

        actualizaHistorial();

        wv.setWebViewClient(new WebViewClient());

        wv.setWebChromeClient(new WebChromeClient() {
            private int progress;

            public void setProgress(int progress) {
                this.progress = progress;
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                pb.setProgress(0);
                pb.setVisibility(View.VISIBLE);
                this.setProgress(progress * 1000);

                pb.incrementProgressBy(progress);

                if (progress == 100) {
                    pb.setVisibility(View.GONE);
                }
            }
        });

        autocompleteTV.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    navega();
                    return true;
                }
                return false;
            }
        });

        lanzaFavorito();
        wv.loadUrl("https://duckduckgo.com/");
        autocompleteTV.setText("duckduckgo.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.atras) {
            wv.goBack();
        }
        if (id == R.id.adelante) {
            wv.goForward();
        }
        if (id == R.id.recargar) {
            wv.reload();
        }
        if (id == R.id.home) {
            wv.loadUrl("https://duckduckgo.com");
            autocompleteTV.setText("duckduckgo.com");
            ocultaTeclado();
        }
        if (id == R.id.gestionarFavoritos) {
            verFavoritos();
        }
        if (id == R.id.agregarFavorito) {
            String url = autocompleteTV.getText().toString();
            if(bd.compruebaFavorito(url) == false && !url.equals("")){
                bd.insertarFavorito(url);
            }
        }

        return super.onOptionsItemSelected(item);

    }

    public void botonNavega(View view){
        navega();
    }

    public void navega(){
        String url = autocompleteTV.getText().toString();

        if(formatoUrl(url)){
            wv.loadUrl(url);
        }else{
            wv.loadUrl("http://" + url);
        }

        ocultaTeclado();

        if(bd.compruebaUrl(url) == false && !url.equals("")){
            bd.insertarURL(url);
            actualizaHistorial();
        }
    }

    public String[] obtenerHistorial(){
        return bd.consultarHistorial();
    }


    public void ocultaTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(wv.getWindowToken(), 0);
    }

    public boolean formatoUrl(String url){
        if(url.contains("http://")){
            return true;
        }else{
            return false;
        }
    }

    public void actualizaHistorial(){
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, obtenerHistorial());
        autocompleteTV.setAdapter(aa);
    }

    public void verFavoritos() {
        Intent i = new Intent(this, Favoritos.class);
        startActivity(i);
    }

    public void lanzaFavorito(){
        favorito = getIntent().getStringExtra("url");

        if(favorito != null){
            if(formatoUrl(favorito)){
                wv.loadUrl(favorito);
            }else{
                wv.loadUrl("http://" + favorito);
            }
            autocompleteTV.setText(favorito);
            wv.requestFocus();
            ocultaTeclado();
        }
    }

}
