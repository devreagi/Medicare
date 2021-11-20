package co.team.blue.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingUp extends AppCompatActivity {

    String UserId;
    private TextInputEditText userEmail, userPassword;
    private MaterialButton register_btn;
    private FirebaseAuth mAuth;
    private ProgressBar register_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //uso de las variables
        userEmail = findViewById(R.id.InputEmail);
        userPassword = findViewById(R.id.InputPassword);
        register_btn = findViewById(R.id.button);
        register_progress = findViewById(R.id.loading);


        // Use de la instancia de firebase

        mAuth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register_btn.setVisibility(View.GONE);
                register_progress.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SingUp.this, "Email y Contraseña pueden estar vacios", Toast.LENGTH_LONG).show();
                    register_btn.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.GONE);
                } else {
                    RegisterUserAccount(email, password);
                }
            }
        });
    }

    private void RegisterUserAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SingUp.this, "Cuenta creada", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SingUp.this, Login.class));
                        } else {
                            Toast.makeText(SingUp.this, "Hubo un error, inténtelo de nuevo", Toast.LENGTH_LONG).show();
                            register_btn.setVisibility(View.VISIBLE);
                            register_progress.setVisibility(View.GONE);
                        }

                    }
                });
    }

}