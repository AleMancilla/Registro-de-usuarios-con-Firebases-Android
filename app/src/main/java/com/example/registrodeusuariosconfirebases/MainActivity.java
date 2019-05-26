package com.example.registrodeusuariosconfirebases;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //barra de progreso
    ProgressDialog progresDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    private EditText editText_user;
    private EditText editText_pass;
    private Button button_registrar;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_user = findViewById(R.id.editText_EmailRegistro);
        editText_pass = findViewById(R.id.editText_contraseniaRegistro);
        button_registrar = findViewById(R.id.button_registroRegistro);
        button_login = findViewById(R.id.button_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //barra de progreso
        progresDialog = new ProgressDialog(this);
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null )
                {
                    //no esta logueado
                    Toast.makeText(MainActivity.this, "EL USUARIO NO ESTA LOGUEADO", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //esta logueado
                    Toast.makeText(MainActivity.this, "EL USUARIO ESTA LOGUEADO", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication() , SegundaPantalla.class);
                    startActivity(intent);
                }
            }
        };

    }

    public void registrarUsuario(View v)
    {
        Intent intent = new Intent(getApplication(), Registro.class);
        startActivity(intent);
    }

    public void login (View v)
    {
        // el trim es para eliminar espacios que tengamos al principio y al final
        String email = editText_user.getText().toString().trim();
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

        //consultar si un usuario existe
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if(task.isSuccessful())
                        {   FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(!user.isEmailVerified() )
                            {
                                Toast.makeText(MainActivity.this, "usuario no verificado...acceda a su correo,,..", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication() , SegundaPantalla.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)//si se presenta una colicion
                            {
                                Toast.makeText(MainActivity.this, "El usuario que intenta registrar ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "No se pudo acceder al usuario, intentelo nuevamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                        progresDialog.dismiss();
                    }
                });

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener!=null)
        {
            firebaseAuth.removeAuthStateListener(listener);
        }
    }
}
