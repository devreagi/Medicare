package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.team.blue.medicare.data.Departamento;
import co.team.blue.medicare.data.Pais;

public class EditarDireccion extends AppCompatActivity {

    Spinner spinnerPaises;
    Spinner spinnerDepartamento;
    Spinner spinnerCiudad;
    Spinner spinnerDireccion;
    DatabaseReference mDatabase;
    String departamentoSeleccionado = "";
    String ciudadSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_direccion);

        spinnerPaises = findViewById(R.id.spinnerPaises);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        spinnerDireccion = findViewById(R.id.spinnerDireccion);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cargarPais();
    }

    //carga paises en spinner
    public void cargarPais() {
        final List<Pais> paises = new ArrayList<>();
        mDatabase.child("pais").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        String id = ds.getKey();
                        paises.add(new Pais(id, nombre));
                    }

                    ArrayAdapter<Pais> arrayAdapter = new ArrayAdapter<>(EditarDireccion.this, android.R.layout.simple_dropdown_item_1line, paises);
                    spinnerPaises.setAdapter(arrayAdapter);
                    spinnerPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cargarDepartamentos(String.valueOf(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //carga departamentos en spinner
    public void cargarDepartamentos(String idPais) {
        final List<Departamento> departamentos = new ArrayList<>();
        mDatabase.child("pais").child(idPais).child("states").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        String id = ds.getKey();
                        departamentos.add(new Departamento(id, nombre));
                    }

                    ArrayAdapter<Departamento> arrayAdapter = new ArrayAdapter<>(EditarDireccion.this, android.R.layout.simple_dropdown_item_1line, departamentos);
                    spinnerDepartamento.setAdapter(arrayAdapter);
                    spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cargarCiudades(idPais, String.valueOf(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //carga ciudades en spinner
    public void cargarCiudades(String idPais, String idDepartamento) {
        final List<Departamento> ciudades = new ArrayList<>();
        mDatabase.child("pais").child(idPais).child("states").child(idDepartamento).child("cities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        String id = ds.getKey();
                        ciudades.add(new Departamento(id, nombre));
                    }

                    ArrayAdapter<Departamento> arrayAdapter = new ArrayAdapter<>(EditarDireccion.this, android.R.layout.simple_dropdown_item_1line, ciudades);
                    spinnerCiudad.setAdapter(arrayAdapter);
                    spinnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ciudadSeleccionada = parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Guardar/ actualzar datos
    public void guardarDatos(View view) {
        boolean guardadoExitoso = false;
        if (guardadoExitoso) {
            Intent editarPerfil = new Intent(this, EditarPerfil.class);
            startActivity(editarPerfil);
        }
    }
}