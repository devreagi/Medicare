package co.team.blue.medicare;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;


public class DetallePedido2 extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    private static final int REQ_CODE_SPEECH_INPUT=100;
    private TextView mEntradaVoz;
    private Button mBotonHablar;
    private Button mBotonEnviar;
    private DatabaseReference mDatabase;
    ArrayList<String> result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

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

        mEntradaVoz=findViewById(R.id.textoEntrada);
        mBotonHablar=findViewById(R.id.botonHablar);
        mBotonEnviar=findViewById(R.id.btnSend);

        mBotonHablar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iniciarEntradaVoz();
            }
        });
        mBotonEnviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
               Enviar();
            }






        });



    }

    private void iniciarEntradaVoz() {

        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hola , que te parecio el servicio? ");

        try     {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch(ActivityNotFoundException e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQ_CODE_SPEECH_INPUT:{

                if(resultCode==RESULT_OK && null != data){
                    result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEntradaVoz.setText(result.get(0));


                }

                break;
            }

        }

    }
    public void Enviar(){
        mDatabase.child("Calificacion de servicio").push().setValue(result.get(0));
        Context context = getApplicationContext();
        CharSequence text = "Reseña Enviada Correctamente !";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mBotonEnviar.setEnabled(false);  //Asigna valor False para deshabilitar .
        mBotonHablar.setEnabled(false);

    }

}
