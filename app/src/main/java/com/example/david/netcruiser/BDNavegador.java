package com.example.david.netcruiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDNavegador extends SQLiteOpenHelper {

    private static String url;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "BD_Navegador.db";
    private static final String ins = "CREATE TABLE URLs (id INTEGER PRIMARY KEY AUTOINCREMENT, url VARCHAR(150))";
    private static final String ins2 = "CREATE TABLE FAVORITOS (id INTEGER PRIMARY KEY AUTOINCREMENT, url VARCHAR(150))";

    public BDNavegador(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ins);
        db.execSQL(ins2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS URLs; DROP TABLE IF EXISTS FAVORITOS");
        onCreate(db);
    }

    public long insertarURL(String url){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("url", url);
            nreg_afectados = db.insert("URLs", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public String[] consultarHistorial(){
        SQLiteDatabase db = getReadableDatabase();
        String[] historial = new String[0];

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("URLs", campos, null, null, null, null, null, null);
            historial = new String[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                historial[i] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return historial;
    }

    public boolean compruebaUrl(String url){
        SQLiteDatabase db = getReadableDatabase();

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("URLs", campos, "url='" + url + "'", null, null, null, null, null);
            if(c.moveToFirst()){
                c.close();
                db.close();
                return true;
            }else {
                c.close();
                db.close();
                return false;
            }
        }
        db.close();
        return false;
    }

    public long insertarFavorito(String url){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("url", url);
            nreg_afectados = db.insert("FAVORITOS", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public String[] consultarFavoritos(){
        SQLiteDatabase db = getReadableDatabase();
        String[] historial = new String[0];

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("FAVORITOS", campos, null, null, null, null, null, null);
            historial = new String[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                historial[i] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return historial;
    }

    public boolean compruebaFavorito(String url){
        SQLiteDatabase db = getReadableDatabase();

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("FAVORITOS", campos, "url='" + url + "'", null, null, null, null, null);
            if(c.moveToFirst()){
                c.close();
                db.close();
                return true;
            }else {
                c.close();
                db.close();
                return false;
            }
        }
        db.close();
        return false;
    }
}