package com.example.registrodeusuariosconfirebases;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private EditText editText_email;
    private EditText editText_pass;
    private EditText editText_nombre;
    private EditText editText_Apellidos;


    private ProgressDialog progresDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        progresDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.editText_EmailRegistro);
        editText_pass = findViewById(R.id.editText_contraseniaRegistro);
        editText_nombre = findViewById(R.id.editText_name_registro);
        editText_Apellidos = findViewById(R.id.editText_lastName_registro);

    }

    public void registrarUsuario(View v)
    {
        // el trim es para eliminar espacios que tengamos al principio y al final
        final String email = editText_email.getText().toString().trim();
        final String pass = editText_pass.getText().toString().trim();
        final String nombre = editText_nombre.getText().toString();
        final String Apellidos = editText_Apellidos.getText().toString();


        // para la base de datos
        final Map<String, Object> userdb = new HashMap<>();


        //verificamos que las cajas de texto no esten vacias
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Debe colocar un Email en la caja de Email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Debe colocar una Contraseña en la caja de contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        //en caso de que se lleno las cajas de usuario y contrasenia
        //se mostrara la barra de progreso
        progresDialog.setMessage("Realizando registro en linea...");
        progresDialog.show();

        //creamos a un nuevo usuario

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Registro.this, "Se registro el usuario Correctamente...", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            // para la base de datos
                                userdb.put("Nombre",nombre);
                                userdb.put("Apellidos", Apellidos);
                                userdb.put("Email",email);
                                userdb.put("Contraseña",pass);
                                userdb.put("IDUser", user.getUid());

                                db.collection("Usuarios")
                                        .add(userdb)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(Registro.this, "Datos agregados correctamente a la base de datos", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // en caso de que falle
                                            }
                                        });
                            //


                            user.sendEmailVerification();
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)//si se presenta una colicion
                            {
                                Toast.makeText(Registro.this, "El usuario que intenta registrar ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Registro.this, "No se pudo registrar el usuario, intentelo nuevamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                        progresDialog.dismiss();
                    }
                });

    }
}
