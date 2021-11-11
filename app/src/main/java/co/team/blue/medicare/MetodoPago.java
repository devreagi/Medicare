package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MetodoPago extends AppCompatActivity {
    TextView tv_mostrarDatos;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_pago);
        this.tv_mostrarDatos = findViewById(R.id.txtScanner);

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

    //metodo onClick
    public void abrirScanner(View view) {
        if (view.getId() == R.id.scanner) {
            new IntentIntegrator(this).initiateScan();
        }
    }

    //llamar metodo result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //llamar a la info
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //obtener info en un String
        String datos = result.getContents();
        tv_mostrarDatos.setText(datos);
    }
}