package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import co.team.blue.medicare.data.Ciudad;
import co.team.blue.medicare.data.Departamento;
import co.team.blue.medicare.data.Nomenclatura;
import co.team.blue.medicare.data.Pais;

public class EditarDireccion extends AppCompatActivity {

    Spinner spinnerPaises;
    Spinner spinnerDepartamento;
    Spinner spinnerCiudad;
    Spinner spinnerDireccion;
    DatabaseReference mDatabase;
    String paisSeleccionado = "";
    String departamentoSeleccionado = "";
    String ciudadSeleccionada = "";
    String direccionSeleccionada = "";
    EditText txtOpcionDir;
    EditText txtNumDir;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_direccion);

        spinnerPaises = findViewById(R.id.spinnerPaises);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        spinnerDireccion = findViewById(R.id.spinnerDireccion);
        txtOpcionDir = findViewById(R.id.txtOpcionDir);
        txtNumDir = findViewById(R.id.txtNumDir);
        btnGuardar = findViewById(R.id.btnGuardarDireccion);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            cargarPais();
            cargarNomenclaturas();
            btnGuardar.setOnClickListener(v -> guardarDireccion(user));
        }
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
                            paisSeleccionado = parent.getItemAtPosition(position).toString();
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
                            departamentoSeleccionado = parent.getItemAtPosition(position).toString();
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
        final List<Ciudad> ciudades = new ArrayList<>();
        mDatabase.child("pais").child(idPais).child("states").child(idDepartamento).child("cities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        String id = ds.getKey();
                        ciudades.add(new Ciudad(id, nombre));
                    }

                    ArrayAdapter<Ciudad> arrayAdapter = new ArrayAdapter<>(EditarDireccion.this, android.R.layout.simple_dropdown_item_1line, ciudades);
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


    // carga nomenclaturas en spinner


    public void cargarNomenclaturas() {
        final List<Nomenclatura> nomenclaturas = new ArrayList<>();
        mDatabase.child("nomenclatura").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = Objects.requireNonNull(ds.child("name").getValue()).toString();
                        String id = ds.getKey();
                        nomenclaturas.add(new Nomenclatura(id, nombre));
                    }

                    ArrayAdapter<Nomenclatura> arrayAdapter = new ArrayAdapter<>(EditarDireccion.this, android.R.layout.simple_dropdown_item_1line, nomenclaturas);
                    spinnerDireccion.setAdapter(arrayAdapter);
                    spinnerDireccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            direccionSeleccionada = parent.getItemAtPosition(position).toString();
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

    //Guardar/ actualzar datos
    private void guardarDireccion(FirebaseUser user) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("pais", paisSeleccionado);
        userMap.put("departamento", departamentoSeleccionado);
        userMap.put("ciudad", ciudadSeleccionada);
        userMap.put("direccion", direccionSeleccionada);
        userMap.put("dirOpcion", txtOpcionDir.getText().toString());
        userMap.put("dirNum", txtNumDir.getText().toString());

        mDatabase.child("usuario").child(user.getUid()).child("ubicacion").updateChildren(userMap);

        Toast.makeText(this, "Direccion actualizada", Toast.LENGTH_SHORT).show();

        Intent editarPerfil = new Intent(this, EditarPerfil.class);
        startActivity(editarPerfil);
    }
}