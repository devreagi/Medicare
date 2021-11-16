package co.team.blue.medicare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfil extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private EditText edtNombre, edtTelefono, edtApellido, edtEmail, edtIdentificacion;

    private DatabaseReference dbReference;

    private Uri imageUri;
    private String myUri = "";
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        dbReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("foto_de_perfil");

        ImageView profileChangeBtn = findViewById(R.id.cambiar_foto_perfil);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        edtNombre = findViewById(R.id.nombre);
        edtApellido = findViewById(R.id.apellido);
        edtTelefono = findViewById(R.id.telefono);
        edtIdentificacion = findViewById(R.id.identificacion);
        edtEmail = findViewById(R.id.email);
        fotoPerfil = findViewById(R.id.imagen_de_perfil);

        btnCancelar.setOnClickListener(v -> startActivity(new Intent(EditarPerfil.this, Perfil.class)));

        btnGuardar.setOnClickListener(v -> validarGuardar());

        profileChangeBtn.setOnClickListener(v -> CropImage.activity().setAspectRatio(1, 1).start(EditarPerfil.this));

        infoUsuario();
    }

    //info ubicacion
    public void irEditarDireccion(View view) {
        Intent direccion = new Intent(this, EditarDireccion.class);
        startActivity(direccion);
    }

    private void validarGuardar() {
        if (TextUtils.isEmpty(edtNombre.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su nombre", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtApellido.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su apellido. ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtTelefono.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su número de telefono. ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtIdentificacion.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su número de identificacion. ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su correo. ", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("nombre", edtNombre.getText().toString());
            userMap.put("apellido", edtApellido.getText().toString());
            userMap.put("telefono", edtTelefono.getText().toString());
            userMap.put("identificacion", edtIdentificacion.getText().toString());
            userMap.put("email", edtEmail.getText().toString());

            dbReference.child("usuario").updateChildren(userMap);

            subirImagen();
        }
    }


    private void infoUsuario() {
        dbReference.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    String nombre = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    String telefono = Objects.requireNonNull(dataSnapshot.child("telefono").getValue()).toString();
                    String apellido = Objects.requireNonNull(dataSnapshot.child("apellido").getValue()).toString();
                    String identificacion = Objects.requireNonNull(dataSnapshot.child("identificacion").getValue()).toString();
                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();

                    edtNombre.setText(nombre);
                    edtTelefono.setText(telefono);
                    edtApellido.setText(apellido);
                    edtIdentificacion.setText(identificacion);
                    edtEmail.setText(email);

                    if (dataSnapshot.hasChild("image")) {
                        String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                        Picasso.get().load(image).into(fotoPerfil);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                fotoPerfil.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                try {
                    throw result.getError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void subirImagen() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cambiar foto de perfil");
        progressDialog.setMessage("Actualizando datos... ");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageReference
                    .child("usuario" + ".jpg");

            StorageTask<UploadTask.TaskSnapshot> uploadTask = fileRef.putFile(imageUri);


            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    dbReference.child("usuario").updateChildren(userMap);

                    progressDialog.dismiss();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Datos actualziados", Toast.LENGTH_SHORT).show();
        }

    }
}