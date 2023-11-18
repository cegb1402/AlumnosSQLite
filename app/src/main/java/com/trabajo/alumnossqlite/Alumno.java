package com.trabajo.alumnossqlite;

import java.io.Serializable;
import java.util.ArrayList;

public class Alumno implements Serializable {
    private int id;
    private String carrera;
    private String nombre;
    private String img;

    private String matricula;

    public Alumno() {

    }

    public Alumno(String matricula,String nombre, String img, String carrera) {
        this.carrera = carrera;
        this.nombre = nombre;
        this.img = img;
        this.matricula = matricula;
    }

    public String getCarrera(){
        return carrera;
    }

    public String getNombre() {
        return nombre;
    }
    public String getImg() {
        return img;
    }
    public String getMatricula() { return matricula; }
    public int getId() {

        return id;
    }

    public void setCarrera(String carrera){
        this.carrera = carrera;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setImg(String img){ this.img = img; }
    public void setMatricula(String matricula){
        this.matricula = matricula;
    }

    public void setId(int id) {
        this.id = id;
    }



}
