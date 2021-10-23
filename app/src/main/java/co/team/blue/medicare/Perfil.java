package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Perfil extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        bottomNavigationView = findViewById(R.id.footer);
        bottomNavigationView.setSelected(true);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.pedidos) {
                startActivity(new Intent(getApplicationContext(), Pedido.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.carrito) {
                startActivity(new Intent(getApplicationContext(), Carrito.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    public void irEditarPerfil(View view) {
        Intent editarPerfil = new Intent(this, EditarPerfil.class);
        startActivity(editarPerfil);
    }

    //boton cerrar sesion
    public void cerrarSesion(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    public void abrirWhatsApp(View view) {
        Intent wpNormal = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        Intent wpAero = getPackageManager().getLaunchIntentForPackage("com.aero");
        Intent wpAerolla = getPackageManager().getLaunchIntentForPackage("com.aerolla");

        if (wpNormal != null) {
            startActivity(wpNormal);
        } else if (wpAero != null) {
            startActivity(wpAero);
        } else if (wpAerolla != null) {
            startActivity(wpAerolla);
        } else {
            Toast.makeText(this, "No tiene  instalaldo WhatsApp", Toast.LENGTH_LONG).show();
        }
    }
}