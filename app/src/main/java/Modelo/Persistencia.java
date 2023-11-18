package Modelo;

import com.example.reclyclerviewymodeladodedatos.Alumno;

public interface Persistencia {
    public void openDataBase();
    public void closeDataBase();
    public long inserAlumno(Alumno alumno);
    public long updateAlumno(Alumno alumno);
    public void deleteAlumnos(int id);
}
