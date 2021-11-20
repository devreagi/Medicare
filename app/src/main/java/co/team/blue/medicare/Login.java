package co.team.blue.medicare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private TextInputEditText userEmail, userPassword;
    private MaterialButton login_btn;
    private ProgressBar register_progress;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Ingresar Email
        userEmail = findViewById(R.id.InputEmail);
        //Ingresar Contraseña
        userPassword = findViewById(R.id.InputPassword);
        //Ingresar al login
        TextView reset_tv = findViewById(R.id.Forgot);
        //Registrar o entrar
        login_btn = findViewById(R.id.button);
        //cargando
        register_progress = findViewById(R.id.loading);

        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        // inicializo firebase

        mAuth = FirebaseAuth.getInstance();
        login_btn.setOnClickListener(v -> {
            login_btn.setVisibility(View.GONE);
            register_progress.setVisibility(View.VISIBLE);

            final String email = Objects.requireNonNull(userEmail.getText()).toString();
            final String password = Objects.requireNonNull(userPassword.getText()).toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Email y Contraseña pueden estar vacios", Toast.LENGTH_LONG).show();
                login_btn.setVisibility(View.VISIBLE);
                register_progress.setVisibility(View.GONE);
            } else {
                loginPerfil(email, password);
            }
        });

        reset_tv.setOnClickListener(v -> ResetPassword());

        //Ingreso con huella

        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, "Huella reconocida");
                startActivity(new Intent(Login.this, Perfil.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "FHuella no reconocida");
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Log.d(TAG, "An unrecoverable error occurred");
                }
            }

        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Huella")
                .setDescription("Ingresa la huella")
                .setNegativeButtonText("Cancel")
                .build();
        findViewById(R.id.finger).setOnClickListener(v -> myBiometricPrompt.authenticate(promptInfo));

    }


    //metodo boton ir a perfil de usuario
    private void loginPerfil(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, Perfil.class));
                    } else {
                        Toast.makeText(Login.this, "Hubo un error, inténtelo de nuevo", Toast.LENGTH_LONG).show();
                        login_btn.setVisibility(View.VISIBLE);
                        register_progress.setVisibility(View.GONE);
                    }

                });
    }


    private void ResetPassword() {

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);

        View view = LayoutInflater.from(Login.this).inflate(R.layout.dialog_edit_text, null);
        TextInputEditText reg_email = view.findViewById(R.id.reset_Email);

        materialAlertDialogBuilder.setTitle("Restablecer contraseña")
                .setMessage("Se enviara un link a su correo para restablecer la contraseña")
                .setView(view)
                .setPositiveButton("Restablecer", (dialog, which) -> {

                    String regemail = Objects.requireNonNull(reg_email.getText()).toString();

                    mAuth.sendPasswordResetEmail(regemail);

                    Toast.makeText(Login.this, "Link enviado, mira el email", Toast.LENGTH_LONG).show();

                }).show();
    }

    // Registrarse pantalla

    public void Registro(View view) {
        Intent signUp = new Intent(Login.this, SingUp.class);
        startActivity(signUp);
    }


}