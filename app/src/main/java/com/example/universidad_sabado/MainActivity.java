package com.example.universidad_sabado;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etcarnet, etnombre, etcarrera, etsemestre;
    CheckBox cbactivo;
    Button btadicionar, btmodificar, btanular, bteliminar;
    String carnet, nombre, carrera, semestre, coleccion="Estudiante", clave ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asociar los objetos java con los objetos XMl
        etcarnet = findViewById(R.id.etcarnet);
        etnombre = findViewById(R.id.etnombre);
        etcarrera = findViewById(R.id.etcarrera);
        etsemestre = findViewById(R.id.etsemestre);
        cbactivo = findViewById(R.id.cbactivo);
        btadicionar = findViewById(R.id.btadicionar);
        btmodificar = findViewById(R.id.btmodificar);
        btanular = findViewById(R.id.btanular);
        bteliminar= findViewById(R.id.bteliminar);
        etcarnet.requestFocus();
    }//Fin metodo Oncreate
    public void Adicionar(View view){

        carnet=etcarnet.getText().toString();
        nombre=etnombre.getText().toString();
        carrera = etcarrera.getText().toString();
        semestre = etsemestre.getText().toString();
        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty() && ! semestre.isEmpty()){
            // Create a new Student with a first and last name
            Map<String, Object> alumno = new HashMap<>();//Debo garantizar una sola clave principal
            alumno.put("Carnet", carnet);
            alumno.put("Nombre", nombre);
            alumno.put("Carrera", carrera);
            alumno.put("Semestre", semestre);
            alumno.put("Activo","Si");


    // Add a new document with a generated ID
            db.collection(coleccion)//Hace referencia al nombre de la tabla
                    .add(alumno)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                           // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento Guardado", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error guardando Documento", Toast.LENGTH_SHORT).show();

                        }
                    });
        }else {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }

    }//Fin metodo Adiccionar

    public void Modificar(View view){
        carnet = etcarnet.getText().toString();
        nombre = etnombre.getText().toString();
        carrera = etcarrera.getText().toString();
        semestre = etsemestre.getText().toString();
        if (!nombre.isEmpty() && !carrera.isEmpty() && !semestre.isEmpty()) {
            // Create a new user with a first and last name
            Map<String, Object> alumno = new HashMap<>();
            alumno.put("Carnet", carnet);
            alumno.put("Nombre", nombre);
            alumno.put("Carrera", carrera);
            alumno.put("Semestre", semestre);

            if (cbactivo.isChecked()){
                alumno.put("Activo","Si");
            }else {
                alumno.put("Activo","No");
            }
            db.collection(coleccion).document(clave)
                    .set(alumno)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this,"Documento Actualizado...",Toast.LENGTH_SHORT).show();
                    LimpiarCampos();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error actualizando Documento...",Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            Toast.makeText(this, "Datos son requeridos", Toast.LENGTH_SHORT).show();
            etnombre.requestFocus();
        }

    }//FIN METODO MODIFICAR

    public void Eliminar(View view){
        db.collection(coleccion).document(clave)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LimpiarCampos();
                        Toast.makeText(MainActivity.this,"Documento eliminado correctamente...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error eliminando documento...",Toast.LENGTH_SHORT).show();
                    }
                });

    }//Fin metodo Eliminar

    public void Anular(View view){
        Map<String, Object> alumno = new HashMap<>();
        alumno.put("Carnet", carnet);
        alumno.put("Nombre", nombre);
        alumno.put("Carrera", carrera);
        alumno.put("Semestre", semestre);
        alumno.put("Activo","No");
        db.collection(coleccion).document(clave)
                .set(alumno)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Documento anulado...",Toast.LENGTH_SHORT).show();
                        LimpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error anulando Documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//Fin metodo Anular

    public void Listar(View view){
        Intent intentestudiantes= new Intent(this,ListarEstudiantesActivity.class);
        startActivity(intentestudiantes);
    }//Fin metodo Listar

    public void Consultar(View view){

        Consultar_documento();
    }//Fin Metodo Consultar

    private void Consultar_documento(){
        carnet= etcarnet.getText().toString();
        if (!carnet.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("Carnet",carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave = document.getId(); //Aqui tomamos el valos hash del documento
                                        etnombre.setText(document.getString("Nombre"));
                                        etcarrera.setText(document.getString("Carrera"));
                                        etsemestre.setText(document.getString("Semestre"));
                                        if (document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }
                                        btanular.setEnabled(true);
                                        bteliminar.setEnabled(true);
                                        btmodificar.setEnabled(true);
                                        cbactivo.setEnabled(true);
                                        btadicionar.setEnabled(false);
                                    }
                                }else {
                                    btadicionar.setEnabled(true);
                                    Toast.makeText(MainActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                }
                                etsemestre.setEnabled(true);
                                etnombre.setEnabled(true);
                                etcarrera.setEnabled(true);
                                etcarnet.setEnabled(false);
                                etnombre.requestFocus();
                            }else {
                                Toast.makeText(MainActivity.this, "Documento no econtrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Carnet requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo consultar documento

    public void Limpiar(View view){
        LimpiarCampos();
    }

    //Metodo para limpiar los campos
    private void LimpiarCampos(){
        etcarnet.setText("");
        etsemestre.setText("");
        etnombre.setText("");
        etcarrera.setText("");
        cbactivo.setChecked(false);
        etcarnet.setEnabled(true);
        etnombre.setEnabled(false);
        etcarrera.setEnabled(false);
        etsemestre.setEnabled(false);
        btadicionar.setEnabled(false);
        btanular.setEnabled(false);
        bteliminar.setEnabled(false);
        btmodificar.setEnabled(false);
        cbactivo.setEnabled(false);
        etcarnet.requestFocus();



    }//Fin limpiar campos

  //Metodo Regresar

    public void Regresar(View view){

        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }



}