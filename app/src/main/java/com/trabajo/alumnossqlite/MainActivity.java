package com.trabajo.alumnossqlite;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    //Declaraciones
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton btnAgregar;
    private FloatingActionButton btnSalir;
    private Aplicacion app;
    private SearchView svBuscar;
    private Adaptador miAdaptador;
    private Alumno alumno;
    private int posicion=-1;
    int REQUEST_CODE = 200;

    // Codigo para mandar solicitud de permisos de imagen al usuario
@RequiresApi(api = Build.VERSION_CODES.M)
private void verificarPermisos()
{
   int permisos= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);

   if (permisos== PackageManager.PERMISSION_GRANTED){
       Toast.makeText(this, "Tu aplicacion cuenta con los servicios de imagen", Toast.LENGTH_SHORT).show();
   }
   else {
       requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},REQUEST_CODE);
   }


}
//Codigo Principal
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mandar  a llmar la funcion para los permisos
      verificarPermisos();


        // Ocultar la barra de notificaciones
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Conexiones con los elementos
        Aplicacion app = (Aplicacion) getApplication();
        recyclerView=(RecyclerView) findViewById(R.id.recId);
        recyclerView.setHasFixedSize(true);
        miAdaptador = app.getAdaptador();
        recyclerView.setAdapter(miAdaptador);
        btnAgregar=(FloatingActionButton) findViewById(R.id.agregarAlumno);
        btnSalir=(FloatingActionButton) findViewById(R.id.salirApp);
        svBuscar = findViewById(R.id.svBuscar);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setupSearchView();

        //Programacion boton agregar
        btnAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alumno=null;
                Intent intent = new Intent(MainActivity.this, AlumnoAlta.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("alumno",alumno);
                bundle.putInt("posicion",posicion);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        //Programacion boton salir
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmar = new AlertDialog.Builder(MainActivity.this);
                confirmar.setTitle("Alumnos");
                confirmar.setMessage("¿Desea salir de la app?");
                confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                confirmar.show();
            }
        });

        //Funcion adaptador
        miAdaptador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                posicion=recyclerView.getChildAdapterPosition(v);
                alumno=app.getAlumnos().get(posicion);
                Intent intent = new Intent(MainActivity.this, AlumnoAlta.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("alumno",alumno);
                bundle.putInt("posicion",posicion);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // Verificar el requestCode para determinar la acción realizada
            if (requestCode == 0) { // Agregar alumno
                // Obtener el alumno agregado desde el intent
                Alumno nuevoAlumno = (Alumno) data.getSerializableExtra("alumno");

                // Añadir el nuevo alumno a la lista completa y notificar al adaptador
                app.getAlumnos().add(nuevoAlumno);
                miAdaptador.notifyDataSetChanged();
            } else if (requestCode == 1) { // Modificar alumno
                // Actualizar el alumno modificado y notificar al adaptador
                miAdaptador.notifyItemChanged(posicion);
            }
        }
    }

    //Search view funcion
    public void setupSearchView() {
        svBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                miAdaptador.filtrar(s);
                return true;
            }
        });
    }
}