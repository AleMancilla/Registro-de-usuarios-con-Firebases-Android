package com.example.registrodeusuariosconfirebases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SegundaPantalla extends AppCompatActivity {

    private Button button_cerrar_sesion;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);

        button_cerrar_sesion = findViewById(R.id.button_cerrar_sesion);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void cerrarSesion(View v)
    {
        firebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }
}
