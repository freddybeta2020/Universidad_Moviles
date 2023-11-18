package com.example.universidad_sabado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarEstudiantesActivity extends AppCompatActivity {

    TextView tvaplicacion;

    String coleccion="Estudiante";
    RecyclerView rvestudiantes;
    ArrayList<ClsEstudiantes> alEstudiantes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estudiantes);
        //Asociar los objetos Java con los objetos XML

        tvaplicacion = findViewById(R.id.tvaplicacion);
        rvestudiantes = findViewById(R.id.rvestudiantes);
        alEstudiantes = new ArrayList<>();

        rvestudiantes.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvestudiantes.setHasFixedSize(true);



        //Consulta y la voy a llevar al ArrayList
        cargar_datos();
    }//Fin metodo onCreate

    private void cargar_datos() {
        db.collection(coleccion)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 //Instanciar la clase Clsetudiantes
                                ClsEstudiantes objestudiantes = new ClsEstudiantes();
                                objestudiantes.setCarnet(document.getString("Carnet"));
                                objestudiantes.setCarrera(document.getString("Carrera"));
                                objestudiantes.setNombre(document.getString("Nombre"));
                                objestudiantes.setActivo(document.getString("Activo"));
                                objestudiantes.setSemestre(document.getString("Semestre"));
                                alEstudiantes.add(objestudiantes);
                                }
                            //Adapate la informacion del ArrayList
                            ClsEstudiantesAdapter aestudientes = new ClsEstudiantesAdapter(alEstudiantes);
                            rvestudiantes.setAdapter(aestudientes);
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void Volver(View view){
        Intent intRegresar= new Intent(this,MainActivity.class);
        startActivity(intRegresar);
    }
}