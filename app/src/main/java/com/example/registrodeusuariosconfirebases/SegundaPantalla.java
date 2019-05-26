package com.example.registrodeusuariosconfirebases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SegundaPantalla extends AppCompatActivity {

    private Button button_cerrar_sesion;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private TextView textView_name, textView_email, textView_urlPhoto, textView_emailVerification, textView_uId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);

        button_cerrar_sesion = findViewById(R.id.button_cerrar_sesion);
        firebaseAuth = FirebaseAuth.getInstance();

        textView_email = findViewById(R.id.textView_email);
        textView_name = findViewById(R.id.textView_username);
        textView_emailVerification = findViewById(R.id.textView_emailVerification);
        textView_urlPhoto = findViewById(R.id.textView_urlphoto);
        textView_uId = findViewById(R.id.textView_IDUSER);

        if(user != null)
        {
            String name = user.getDisplayName();
            String email = user.getEmail();
            //String urlPhoto = user.getPhotoUrl().toString();

            //boolean emailVeridied = user.isEmailVerified();
            String idUser = user.getUid();

            //String emailVer=emailVeridied +" ";

            textView_name.setText(name);
            textView_email.setText(email);
            //textView_emailVerification.setText(emailVer);
            //textView_urlPhoto.setText(urlPhoto);
            textView_uId.setText(idUser);



        }

    }

    public void cerrarSesion(View v)
    {
        firebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }
}
