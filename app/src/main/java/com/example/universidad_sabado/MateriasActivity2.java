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

import java.util.HashMap;
import java.util.Map;

public class MateriasActivity2 extends AppCompatActivity {

    EditText etcodigoMateria, etmateria, etcreditos, etprofesor;
    CheckBox cbactivo;
    Button  btconsultar, btguardar, btcancelar, btactivar, btregresar;
    String codigoMateria, materia, creditos, profesor, coleccion="Materias", clave ;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias2);

        //Asociar los objetos java con los objetos XMl
        etcodigoMateria = findViewById(R.id.etcodigomateria);
        etmateria = findViewById(R.id.etmateria);
        etcreditos = findViewById(R.id.etcreditos);
        etprofesor = findViewById(R.id.etprofesor);
        cbactivo = findViewById(R.id.cbactivo);
        btconsultar = findViewById(R.id.btconsultar);
        btguardar = findViewById(R.id.btguardar);
        btcancelar = findViewById(R.id.btcancelar);
        btactivar = findViewById(R.id.btactivar);
        btregresar = findViewById(R.id.btregresar);
    }//Fin metodo Oncreate


    public void Guardar(View view){

        codigoMateria =etcodigoMateria.getText().toString();
        materia=etmateria.getText().toString();
        creditos = etcreditos.getText().toString();
        profesor = etprofesor.getText().toString();
        if (!codigoMateria.isEmpty() && !materia.isEmpty() && !creditos.isEmpty() && ! profesor.isEmpty()){
            // Create a new Student with a first and last name
            Map<String, Object> Materias = new HashMap<>();//Debo garantizar una sola clave principal
            Materias.put("Codigo_Materia", codigoMateria);
            Materias.put("Materia", materia);
            Materias.put("Creditos", creditos);
            Materias.put("Profesor", profesor);
            Materias.put("Activo","Si");


            // Add a new document with a generated ID
            db.collection(coleccion)//Hace referencia al nombre de la tabla
                    .add(Materias)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MateriasActivity2.this, "Documento Guardado", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MateriasActivity2.this, "Error guardando Documento", Toast.LENGTH_SHORT).show();

                        }
                    });
        }else {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigoMateria.requestFocus();
        }

    }//Fin metodo Adiccionar

    //Funcion Activar
    public void Activar(View view){
        codigoMateria = etcodigoMateria.getText().toString();
        materia = etmateria.getText().toString();
        creditos = etcreditos.getText().toString();
        profesor = etprofesor.getText().toString();
        if (!materia.isEmpty() && !creditos.isEmpty() && !profesor.isEmpty()) {
            // Create a new user with a first and last name
            Map<String, Object> Materias = new HashMap<>();
            Materias.put("Codigo_Materia", codigoMateria);
            Materias.put("Materia", materia);
            Materias.put("Creditos", creditos);
            Materias.put("Profesor", profesor);

            if (cbactivo.isChecked()){
                Materias.put("Activo","Si");
            }else {
                Materias.put("Activo","No");
            }

            db.collection(coleccion).document(clave)
                    .set(Materias)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MateriasActivity2.this,"Documento Actualizado...",Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MateriasActivity2.this,"Error actualizando Documento...",Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            Toast.makeText(this, "Datos son requeridos", Toast.LENGTH_SHORT).show();
            etmateria.requestFocus();
        }

    }//FIN METODO MODIFICAR
    public void Consultar(View view){

        Consultar_documento();
    }//Fin Metodo Consultar

    private void Consultar_documento(){
        codigoMateria= etcodigoMateria.getText().toString();
        if (!codigoMateria.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("Codigo_Materia",codigoMateria)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave = document.getId(); //Aqui tomamos el valos hash del documento
                                        etmateria.setText(document.getString("Materia"));
                                        etcreditos.setText(document.getString("Creditos"));
                                        etprofesor.setText(document.getString("Profesor"));
                                        if (document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }
                                        btcancelar.setEnabled(true);
                                        btactivar.setEnabled(true);
                                        cbactivo.setEnabled(true);
                                        btguardar.setEnabled(true);
                                    }
                                }else {
                                    btguardar.setEnabled(true);
                                    Toast.makeText(MateriasActivity2.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                }
                                etmateria.setEnabled(true);
                                etcreditos.setEnabled(true);
                                etprofesor.setEnabled(true);
                                etcodigoMateria.setEnabled(false);
                                etmateria.requestFocus();
                                btcancelar.setEnabled(true);
                            }else {
                                Toast.makeText(MateriasActivity2.this, "Documento no econtrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Codigo de la materia", Toast.LENGTH_SHORT).show();
            etcodigoMateria.requestFocus();
        }
    }//Fin metodo consultar documento

    public void Limpiar(View view){
        LimpiarCampos();
    }

    //Metodo para limpiar los campos
    private void LimpiarCampos(){
        etcodigoMateria.setText("");
        etmateria.setText("");
        etcreditos.setText("");
        etprofesor.setText("");
        cbactivo.setChecked(false);
        etcodigoMateria.setEnabled(true);
        etmateria.setEnabled(false);
        etcreditos.setEnabled(false);
        etprofesor.setEnabled(false);
        btguardar.setEnabled(false);
        btcancelar.setEnabled(false);
        btactivar.setEnabled(false);
        cbactivo.setEnabled(false);
        etcodigoMateria.requestFocus();



    }//Fin limpiar campos

    public void Retornar(View view){

        Intent intent = new Intent(MateriasActivity2.this, MenuActivity.class);
        startActivity(intent);
    }
}