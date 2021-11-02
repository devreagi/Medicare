package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditarDireccion extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_direccion);
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