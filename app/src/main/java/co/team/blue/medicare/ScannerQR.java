package co.team.blue.medicare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerQR extends AppCompatActivity {

    TextView tv_mostrarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);
        this.tv_mostrarDatos = findViewById(R.id.txt);
    }
    //metodo onClick
    public void abrirScanner(View view) {
        if(view.getId() == R.id.AbrirScanner){
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