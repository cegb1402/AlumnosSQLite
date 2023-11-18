package com.trabajo.alumnossqlite;

import android.app.Application;
import android.util.Log;
import java.util.ArrayList;
import Modelo.AlumnosDb;

public class Aplicacion extends Application {
    static ArrayList<Alumno> alumnos;
    private Adaptador adaptador;

    private AlumnosDb alumnosDb;

    public ArrayList<Alumno> getAlumnos(){ return alumnos; }

    public Adaptador getAdaptador(){ return adaptador; }

    @Override
    public void onCreate(){
        super.onCreate();
        alumnosDb = new AlumnosDb(getApplicationContext());
        alumnos = alumnosDb.allAlumnos();
        alumnosDb.openDataBase();

        adaptador = new Adaptador(alumnos, this);
        Log.d("", "onCreate: tamaño array list " + alumnos.size());
    }

}
