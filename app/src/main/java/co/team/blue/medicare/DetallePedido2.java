package co.team.blue.medicare;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class DetallePedido2 extends AppCompatActivity {


    private static final int REQ_CODE_SPEECH_INPUT = 100;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> result;
    private TextView mEntradaVoz, mNumero, mFecha, mSubtotal, mTotal, mMp;
    private Button mBotonHablar;
    private Button mBotonEnviar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        bottomNavigationView = findViewById(R.id.footer);
        bottomNavigationView.setSelected(true);

        mNumero = (TextView) findViewById(R.id.tvNumero);
        mFecha = (TextView) findViewById(R.id.tvFecha);
        mSubtotal = (TextView) findViewById(R.id.tvSubtotal);
        mTotal = (TextView) findViewById(R.id.tvTotal);
        mMp = (TextView) findViewById(R.id.tvMp);

        mDatabase.child("Pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {

                    String numero = snapshot.child("Numero").getValue().toString();
                    mNumero.setText(numero);

                    String fecha = snapshot.child("Fecha").getValue().toString();
                    mFecha.setText(fecha);

                    String subtotal = snapshot.child("Subtotal").getValue().toString();
                    mSubtotal.setText(subtotal);

                    String total = snapshot.child("Total").getValue().toString();
                    mTotal.setText(total);

                    String mp = snapshot.child("Mp").getValue().toString();
                    mMp.setText(mp);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

        mEntradaVoz = findViewById(R.id.textoEntrada);
        mBotonHablar = findViewById(R.id.botonHablar);
        mBotonEnviar = findViewById(R.id.btnSend);

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

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hola , que te parecio el servicio? ");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            System.out.println("ERROR GRABANDO: " + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_SPEECH_INPUT: {

                if (resultCode == RESULT_OK && null != data) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEntradaVoz.setText(result.get(0));
                }
                break;
            }

        }

    }

    public void Enviar() {
        if (result == null) {
            Toast toast = Toast.makeText(this, "Graba un mensaje primero", Toast.LENGTH_SHORT);
            toast.show();
        } else {
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

}
