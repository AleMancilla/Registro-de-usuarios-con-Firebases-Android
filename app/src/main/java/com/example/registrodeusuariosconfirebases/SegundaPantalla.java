package com.example.registrodeusuariosconfirebases;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SegundaPantalla extends AppCompatActivity {

    private Button button_cerrar_sesion;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private TextView textView_name,textView_Lastname, textView_email, textView_urlPhoto, textView_emailVerification, textView_uId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);

        button_cerrar_sesion = findViewById(R.id.button_cerrar_sesion);
        firebaseAuth = FirebaseAuth.getInstance();

        textView_email = findViewById(R.id.textView_email);
        textView_name = findViewById(R.id.textView_username);
        textView_Lastname=findViewById(R.id.textView_lastname);
        textView_emailVerification = findViewById(R.id.textView_emailVerification);
        textView_urlPhoto = findViewById(R.id.textView_urlphoto);
        textView_uId = findViewById(R.id.textView_IDUSER);

        if(user != null)
        {
            //String name = user.getDisplayName();
            final String uid = user.getUid();
            //String idUser = user.getUid();



            //leendo la base de datos


            db.collection("Usuarios").whereEqualTo("IDUser", uid)
            .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("IDUser").toString().equals(uid))
                                    {
                                        textView_name.setText(document.getData().get("Nombre").toString());
                                        textView_email.setText(document.getData().get("Email").toString());
                                        textView_uId.setText(document.getData().get("IDUser").toString());
                                        textView_Lastname.setText(document.getData().get("Apellidos").toString());

                                    }
                                    Log.d("Aleman", document.getId() + " => " + document.getData());
                                }

                                //Toast.makeText(getApplication(), "EL EMAIL BUSCADO ES "+email, Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                //error al adquirir los documentos
                            }
                        }
                    });
            // termino de leer la base de datos





        }

    }

    public void cerrarSesion(View v)
    {
        firebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }
}
