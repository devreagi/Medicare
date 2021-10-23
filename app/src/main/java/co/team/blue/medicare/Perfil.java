package co.team.blue.medicare;

import android.content.Intent;
import android.net.Uri;
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

    //ir  a editar campos del perfil
    public void irEditarPerfil(View view) {
        //Intent editarPerfil = new Intent(this, EditarPerfil.class);
        //startActivity(editarPerfil);
        Toast.makeText(this, "TRABAJO EN PROGRESO", Toast.LENGTH_LONG).show();
    }

    //boton cerrar sesion
    public void cerrarSesion(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    //boton abrir whatsapp/soporte
    public void abrirWhatsApp(View view) {
        String number = "+57 1234567890";
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    //Abrir play store
    public void abrirPlayStore(View view) {
        Intent playStore = getPackageManager().getLaunchIntentForPackage("com.android.vending");

        if (playStore != null) {
            startActivity(playStore);
        } else {
            Toast.makeText(this, "Error al abrir la tienda de  aplicaciones", Toast.LENGTH_LONG).show();
        }
    }

    //boton mis compras
    public void irPedidos(View view) {
        Intent pedidos = new Intent(this, Pedido.class);
        startActivity(pedidos);
    }
}