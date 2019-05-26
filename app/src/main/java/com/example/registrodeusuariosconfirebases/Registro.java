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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {
    private EditText editText_email;
    private EditText editText_pass;

    private ProgressDialog progresDialog;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        progresDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.editText_EmailRegistro);
        editText_pass = findViewById(R.id.editText_contraseniaRegistro);

    }

    public void registrarUsuario(View v)
    {
        // el trim es para eliminar espacios que tengamos al principio y al final
        String email = editText_email.getText().toString().trim();
        String pass = editText_pass.getText().toString().trim();

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
