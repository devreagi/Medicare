package co.team.blue.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity{
    private static final String TAG_ = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String TAG = MainActivity.class.getName();
    private TextInputEditText userEmail,userPassword;
    private MaterialButton login_btn;
    private TextView reset_tv;
    private ProgressBar register_progress;
    String UserId;
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
        reset_tv= findViewById(R.id.Forgot);
        //Registrar o entrar
        login_btn = findViewById(R.id.button);
        //cargando
        register_progress = findViewById(R.id.loading);

        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        // inicializo firebase

        mAuth = FirebaseAuth.getInstance();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setVisibility(View.GONE);
                register_progress.setVisibility(View.VISIBLE);

                final  String email = userEmail.getText().toString();
                final  String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Email y Contraseña pueden estar vacios", Toast.LENGTH_LONG).show();
                    login_btn.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.GONE);
                }else{
                    loginPerfil(email,password);
                }
            }
        });

        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });

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

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
                    Log.d(TAG, "An unrecoverable error occurred");
                }
            }

        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Huella")
                .setDescription("Ingresa la huella")
                .setNegativeButtonText("Cancel")
                .build();
        findViewById(R.id.finger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);
            }
        });

    }


    //metodo boton ir a perfil de usuario
    private void loginPerfil(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, Perfil.class));
                        } else {
                            Toast.makeText(Login.this, "Hubo un error, inténtelo de nuevo", Toast.LENGTH_LONG).show();
                            login_btn.setVisibility(View.VISIBLE);
                            register_progress.setVisibility(View.GONE);
                        }

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
                .setPositiveButton("Restablecer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String regemail = reg_email.getText().toString();

                        mAuth.sendPasswordResetEmail(regemail);

                        Toast.makeText(Login.this, "Link enviado, mira el email", Toast.LENGTH_LONG).show();

                    }
                }).show();
    }

    // Registrarse pantalla

    public void Registro(View view) {
        Intent signUp = new Intent(Login.this, SingUp.class);
        startActivity(signUp);
    }


}