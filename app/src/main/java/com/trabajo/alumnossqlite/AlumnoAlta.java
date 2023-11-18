package com.trabajo.alumnossqlite;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import Modelo.AlumnosDb;

public class AlumnoAlta extends AppCompatActivity {

    //Declaracion
    private RecyclerView recyclerView;
    private Button btnGuardar, btnRegresar, btnEliminar,btnLimpiar;
    private Alumno alumno;
    private EditText txtNombre, txtMatricula, txtGrado;
    private ImageView imgAlumno;
    private TextView lblImagen;
    private String pathString;
    private int posicion;
    private AlumnosDb alumnosDb;

    //Funcion para cargar la imagen
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione una apicacion"),10);
    }
//funcion para obtener el path de la imagen y mostrarla
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            imgAlumno.setImageURI(path);
            pathString=path.toString();
            lblImagen.setText(pathString);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_alta);

        //Conexiones con los elementos
        btnLimpiar=findViewById(R.id.btnLimpiar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        txtMatricula = (EditText) findViewById(R.id.txtMatricula);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtGrado = (EditText) findViewById(R.id.txtCarrera);
        imgAlumno = (ImageView) findViewById(R.id.imgAlumno);
        lblImagen = (TextView) findViewById(R.id.lblFoto);

        Bundle bundle = getIntent().getExtras();
        alumno = (Alumno) bundle.getSerializable("alumno");
        posicion = bundle.getInt("posicion", posicion);

        if(posicion >= 0 && alumno!=null){
            txtMatricula.setText(alumno.getMatricula());
            txtNombre.setText(alumno.getNombre());
            txtGrado.setText(alumno.getCarrera());

            String imgUriString = alumno.getImg();

            if (imgUriString != null) {
                Uri imgUri = Uri.parse(imgUriString);
                imgAlumno.setImageURI(imgUri);
                lblImagen.setText(imgUriString);
            } else {
                imgAlumno.setImageDrawable(getDrawable(R.drawable.icon_user));
            }
        }

        //Evento para cargar la imagen presionando la imagen
        imgAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGrado.setText("");
                txtMatricula.setText("");
                txtNombre.setText("");
                imgAlumno.setImageResource(R.drawable.icon_user);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMatricula.getText().toString().isEmpty() || txtNombre.getText().toString().isEmpty() || txtGrado.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Faltó capturar datos", Toast.LENGTH_SHORT).show();
                } else {
                    if (posicion >= 0 && alumno != null) {
                        // Modificar Alumno
                        alumno.setMatricula(txtMatricula.getText().toString());
                        alumno.setNombre(txtNombre.getText().toString());
                        alumno.setCarrera(txtGrado.getText().toString());
                        // Cargar la imagen si es diferente a nulo
                        if (pathString != null) {
                            alumno.setImg(pathString);
                            lblImagen.setText(pathString);
                            Toast.makeText(getApplicationContext(), "Se modificó con éxito ", Toast.LENGTH_SHORT).show();
                        }
                        ((Aplicacion) getApplication()).getAlumnos().get(posicion).setMatricula(alumno.getMatricula());
                        ((Aplicacion) getApplication()).getAlumnos().get(posicion).setNombre(alumno.getNombre());
                        ((Aplicacion) getApplication()).getAlumnos().get(posicion).setCarrera(alumno.getCarrera());
                        ((Aplicacion) getApplication()).getAlumnos().get(posicion).setImg(alumno.getImg());

                        alumnosDb = new AlumnosDb(getApplicationContext());
                        alumnosDb.updateAlumno(alumno);
                        Toast.makeText(getApplicationContext(), "Se modificó con éxito ", Toast.LENGTH_SHORT).show();
                        ((Aplicacion) getApplication()).getAdaptador().notifyDataSetChanged();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        // Agregar un nuevo alumno
                        String matricula = txtMatricula.getText().toString();
                        String nombre = txtNombre.getText().toString();
                        String grado = txtGrado.getText().toString();
                        String imagen = pathString;

                        if (matricula.isEmpty() || nombre.isEmpty() || grado.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Faltó capturar datos", Toast.LENGTH_SHORT).show();
                            txtMatricula.requestFocus();
                        } else {
                            alumno = new Alumno();
                            alumno.setMatricula(matricula);
                            alumno.setNombre(nombre);
                            alumno.setCarrera(grado);
                            alumno.setImg(imagen);
                            alumnosDb = new AlumnosDb(getApplicationContext());
                            // Agregar el alumno nuevo a la bd y obtener el ID que se le asigno
                            long idNuevo = alumnosDb.inserAlumno(alumno);

                            if (idNuevo != -1) {
                                // Asignar el ID al alumno nuevo
                                alumno.setId((int) idNuevo);

                                // Cargar la imagen si es diferente a nulo
                                if (pathString != null) {
                                    alumno.setImg(pathString);
                                    lblImagen.setText(pathString);
                                }

                                // Agregar el alumno nuevo al adaptador
                                ((Aplicacion) getApplication()).getAdaptador().addAlumno(alumno);

                                // Notificar al adaptador del cambio en los datos
                                ((Aplicacion) getApplication()).getAdaptador().notifyItemInserted(((Aplicacion) getApplication()).getAlumnos().size() - 1);


                                Toast.makeText(getApplicationContext(), "Se guardó con éxito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al guardar alumno", Toast.LENGTH_SHORT).show();
                            }
                        }
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
            }
        });

//Progracion botn regresar
        btnRegresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
//Progracion botn eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(posicion >= 0 && alumno!=null){

                    AlertDialog.Builder confirmar = new AlertDialog.Builder(AlumnoAlta.this);
                    confirmar.setTitle("Eliminar Alumnos");
                    confirmar.setMessage("¿Desea eliminar al alumno?");
                    confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlumnosDb alumnosDb = new AlumnosDb(getApplicationContext());
                            alumnosDb.deleteAlumnos(alumno.getId());
                            ((Aplicacion) getApplication()).getAdaptador().removeAlumno(posicion);
                            setResult(Activity.RESULT_OK);
                            finish();

                            Toast.makeText(getApplicationContext(), "Se eliminó con éxito", Toast.LENGTH_SHORT).show();
                        }
                    });
                    confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //No hace nada
                        }
                    });
                    confirmar.show();
                }else{
                    Toast.makeText(getApplicationContext(), "No se puede eliminar el alumno", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

