package co.team.blue.medicare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtener localizacion actual y subir a firebase
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //metodo boton ir a perfil de usuario
    //modificado temporalmente para ir a login.
    public void irPerfilUsuario(View view) {
        Intent perfil = new Intent(this, Login.class);
        startActivity(perfil);
    }

    //metodo boton ir a catalogo
    public void btnIrCatalogo(View view) {
        Intent catalogo = new Intent(this, Catalogo.class);
        startActivity(catalogo);
    }

    //metodo boton ir a ubicacion pedido
    public void btnIrUbicacion(View view) {
        Intent ubicacion = new Intent(this, Mapa10.class);
        startActivity(ubicacion);
    }

    //metodo boton ir a pedidos
    public void btnIrPedidos(View view) {
        Intent pedidos = new Intent(this, Pedido.class);
        startActivity(pedidos);
    }
}