package co.team.blue.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pago extends AppCompatActivity {

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
boton=(Button) findViewById(R.id.scanner);
boton.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view) {

startActivity(new Intent(Pago.this, ScannerQR.class));

    }
    });




    }
}