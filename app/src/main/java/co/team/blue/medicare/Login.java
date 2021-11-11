package co.team.blue.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    private TextInputEditText userEmail,userPassword;
    private MaterialButton register_btn;
    private TextView login_tv;
    //private FirebaseAuth mAuth;
    private ProgressBar register_progress;
    String UserId;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setContentView(R.layout.activity_login);
        //Ingresar Email
        userEmail = findViewById(R.id.InputEmail);
        //Ingresar Contraseña
        userPassword = findViewById(R.id.InputPassword);
        //Ingresar al login
        login_tv= findViewById(R.id.Forgot);
        //Registrar o entrar
        register_btn = findViewById(R.id.button);
        //cargando
        register_progress = findViewById(R.id.loading);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_btn.setVisibility(View.GONE);
                register_progress.setVisibility(View.VISIBLE);

                final  String email = userEmail.getText().toString();
                final  String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Email y Contraseña pueden estar vacios", Toast.LENGTH_LONG).show();
                    register_btn.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.GONE);
                }else{
                    loginPerfil();
                }
            }
        });

    }


    //metodo boton ir a perfil de usuario
    private void loginPerfil() {

        Intent perfil = new Intent(this, Perfil.class);
        startActivity(perfil);
    }


}