package com.trabajo.alumnossqlite;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> implements View.OnClickListener, Filterable {
    protected ArrayList<Alumno> listaAlumnos;

    private ArrayList<Alumno> nFilteredAlumno; // Variable para SearchView

    private View.OnClickListener listener;
    private Context context;
    private LayoutInflater inflater;

    public Adaptador(ArrayList<Alumno> listaAlumnos, Context context) {
        this.listaAlumnos = listaAlumnos;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.nFilteredAlumno = new ArrayList<>(listaAlumnos); // Asignar la lista completa

    }

    //Funcion para insertar alumnos en tiempo real, y actualizar la lista
    public void addAlumno(Alumno alumno) {
        listaAlumnos.add(alumno);
        nFilteredAlumno.add(alumno);
        notifyItemInserted(listaAlumnos.size() - 1);
    }
    //Funcion para borrar alumnos en tiempo real, y actualizar la lista
    public void removeAlumno(int position) {
        Alumno alumnoToRemove = listaAlumnos.get(position);
        listaAlumnos.remove(position);
        nFilteredAlumno.remove(alumnoToRemove);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaAlumnos.size());  // Para actualizar números de posición en la interfaz de usuario
    }

    public void filtrar(String texto) {
        listaAlumnos.clear();

        if (texto.isEmpty()) {
            listaAlumnos.addAll(nFilteredAlumno);
        } else {
            texto = texto.toLowerCase();
            for (Alumno alumno : nFilteredAlumno) {
                // Verificar si el nombre del alumno contiene el texto de búsqueda
                if (alumno.getMatricula().toLowerCase().contains(texto) ||
                        alumno.getNombre().toLowerCase().contains(texto) ||
                        alumno.getCarrera().toLowerCase().contains(texto)) {
                    listaAlumnos.add(alumno);  // Agregar el alumno a la lista
                }
            }
        }

        notifyDataSetChanged();  // Notificar al RecyclerView que los datos han cambiado
    }

    @NonNull
    @NotNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alumnos_items, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Adaptador.ViewHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);
        holder.txtMatricula.setText(alumno.getMatricula());
        holder.txtNombre.setText(alumno.getNombre());
        holder.txtCarrera.setText(alumno.getCarrera());

        if (alumno.getImg() != null) {
            holder.idImagen.setImageURI(Uri.parse(alumno.getImg()));
        } else {
            // Establecer una imagen de reemplazo o dejarlo vacío según sea necesario
            holder.idImagen.setImageResource(R.drawable.icon_user);
        }
    }

    @Override
    public int getItemCount() { return listaAlumnos != null ? listaAlumnos.size() : 0; }

    public void setOnClickListener(View.OnClickListener listener) { this.listener = listener; }

    @Override
    public void onClick(View v) { if(listener != null) listener.onClick(v); }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Alumno> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(nFilteredAlumno);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Alumno alumnoF : nFilteredAlumno) {
                        if (alumnoF.getMatricula().toLowerCase().contains(filterPattern) ||
                                alumnoF.getNombre().toLowerCase().contains(filterPattern) ||
                                alumnoF.getCarrera().toLowerCase().contains(filterPattern)) {
                            filteredList.add(alumnoF);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaAlumnos.clear();
                listaAlumnos.addAll((ArrayList<Alumno>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutInflater inflater;
        private TextView txtNombre;
        private TextView txtMatricula;
        private TextView txtCarrera;
        private ImageView idImagen;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombreAlumno);
            txtMatricula = (TextView) itemView.findViewById(R.id.txtMatricula);
            txtCarrera = (TextView) itemView.findViewById(R.id.txtCarrera);

            idImagen = (ImageView) itemView.findViewById(R.id.foto);
        }
    }
}
