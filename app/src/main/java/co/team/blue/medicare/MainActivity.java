package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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