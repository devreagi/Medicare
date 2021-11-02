package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
    }

    //info ubicacion
    public void irEditarDireccion(View view) {
        Intent direccion = new Intent(this, EditarDireccion.class);
        startActivity(direccion);
    }

    //cerrar edicion
    public void irPerfilUsuario(View view) {
        Intent perfil = new Intent(this, Perfil.class);
        startActivity(perfil);
    }

    //Guardar/ actualzar datos
    public void guardarDatos(View view) {
        boolean guardadoExitoso = false;
        if (guardadoExitoso) {
            Intent perfil = new Intent(this, Perfil.class);
            startActivity(perfil);
        } else {
            Toast.makeText(this, "HUBO UN ERROR GUARDANDO LA DIRECCION ", Toast.LENGTH_LONG).show();
        }
    }
}