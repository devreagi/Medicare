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
        subirLatLongfirebase();


    }
    private void subirLatLongfirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("latitud: ",+location.getLatitude()+"Longitud: "+location.getLongitude());
                            Map<String,Object> latlang =  new HashMap<>();
                            latlang.put("latitud",location.getLatitude());
                            latlang.put("longitud",location.getLongitude());
                            mDatabase.child("usuarios").push().setValue(latlang);
                        }
                    }
                });
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