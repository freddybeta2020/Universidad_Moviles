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
import android.widget.TextView;
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

public class MatriculaActivity extends AppCompatActivity {

    EditText etmatricula, etfecha, etcarnet, etmateria;
    Button btconsultarMatricula, btconsultarCarnet, btconsultarMateria, btadiccionar,
    btanular, btlimpiar, btregresar;
    TextView tvnombre, tvmateria;
    CheckBox cbactivo;
    String matricula, fecha, carnet,nombre, codigo_materia, nombre_materia, coleccion="Estudiante", clave, materias = "Materias", MatriculasEstudiante = "Matriculas";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matricula);

        //Asociar los objetos java con los objetos XMl

        etmatricula = findViewById(R.id.etmatricula);
        etfecha = findViewById(R.id.etfecha);
        etcarnet = findViewById(R.id.etcarnet);
        etmateria = findViewById(R.id.etmateria);
        btconsultarMatricula = findViewById(R.id.btconsultarmatricula);
        btconsultarCarnet = findViewById(R.id.btconsultarcarnet);
        btconsultarMateria = findViewById(R.id.btconsultarmateria);
        btadiccionar = findViewById(R.id.btadicionar);
        btanular = findViewById(R.id.btanular);
        btlimpiar = findViewById(R.id.btlimpiar);
        btregresar = findViewById(R.id.btregresar);
        tvnombre = findViewById(R.id.tvnombre);
        tvmateria = findViewById(R.id.tvmateria);
        cbactivo = findViewById(R.id.cbactivo);

    }

    public void Adicionar(View view){
        Guardar();
    }

    //Metodo para Guardar las matriculas
    private void Guardar(){

        matricula =etmatricula.getText().toString();
        codigo_materia = etmateria.getText().toString();
        fecha = etfecha.getText().toString();
        carnet = etcarnet.getText().toString();
        nombre = tvnombre.getText().toString();
        nombre_materia = tvmateria.getText().toString();

        if (!matricula.isEmpty() && !codigo_materia.isEmpty() && !fecha.isEmpty() && ! carnet.isEmpty() && !nombre.isEmpty() && !nombre_materia.isEmpty()){
            // Create a new Student with a first and last name
            Map<String, Object> Matriculas = new HashMap<>();//Debo garantizar una sola clave principal
            Matriculas.put("carnet", carnet);
            Matriculas.put("codigo_matricula", matricula);
            Matriculas.put("codigo_materia", codigo_materia);
            Matriculas.put("fecha", fecha);
            Matriculas.put("nombre_estudiante",nombre);
            Matriculas.put("nombre_materia",nombre_materia);
            Matriculas.put("Activo","Si");


            // Add a new document with a generated ID
            db.collection(MatriculasEstudiante)//Hace referencia al nombre de la tabla
                    .add(Matriculas)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MatriculaActivity.this, "Documento Guardado", Toast.LENGTH_SHORT).show();

                            btconsultarCarnet.setEnabled(false);
                            btconsultarMateria.setEnabled(false);
                            LimpiarCampos();
                            etmatricula.requestFocus();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MatriculaActivity.this, "Error guardando Documento", Toast.LENGTH_SHORT).show();

                        }
                    });
        }else {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etmatricula.requestFocus();
        }

    }//Fin metodo Guardar Matriculas

    //Funcion Activar
    public void Activar(View view) {
        matricula = etmatricula.getText().toString();
        codigo_materia = etmateria.getText().toString();
        fecha = etfecha.getText().toString();
        carnet = etcarnet.getText().toString();
        nombre = tvnombre.getText().toString();
        nombre_materia = tvmateria.getText().toString();

        if (!matricula.isEmpty() && !codigo_materia.isEmpty() && !fecha.isEmpty() && !carnet.isEmpty() && !nombre.isEmpty() && !nombre_materia.isEmpty()) {
            // Create a new user with a first and last name
            Map<String, Object> Matriculas = new HashMap<>();//Debo garantizar una sola clave principal
            Matriculas.put("carnet", carnet);
            Matriculas.put("codigo_matricula", matricula);
            Matriculas.put("codigo_materia", codigo_materia);
            Matriculas.put("fecha", fecha);
            Matriculas.put("nombre_estudiante", nombre);
            Matriculas.put("nombre_materia", nombre_materia);

            if (cbactivo.isChecked()) {
                Matriculas.put("Activo", "Si");
            } else {
                Matriculas.put("Activo", "No");
            }

            db.collection(MatriculasEstudiante).document(clave)
                    .set(Matriculas)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MatriculaActivity.this, "Documento Actualizado...", Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MatriculaActivity.this, "Error actualizando Documento...", Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(this, "Datos son requeridos", Toast.LENGTH_SHORT).show();
            etmateria.requestFocus();
        }
    }

    public void ConsultarMatricula(View view){
        Consultar_Matricula();
    }

    //Metodo para consultar la matricula
    private void Consultar_Matricula(){
        matricula= etmatricula.getText().toString();
        if (!matricula.isEmpty()){
            db.collection(MatriculasEstudiante)
                    .whereEqualTo("codigo_matricula",matricula)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave = document.getId(); //Aqui tomamos el valos hash del documento
                                        etmateria.setText(document.getString("codigo_materia"));
                                        etfecha.setText(document.getString("fecha"));
                                        etcarnet.setText(document.getString("carnet"));
                                        tvnombre.setText(document.getString("nombre_estudiante"));
                                        tvmateria.setText(document.getString("nombre_materia"));
                                        if (document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }
                                        btadiccionar.setEnabled(false);
                                        btanular.setEnabled(true);
                                        cbactivo.setEnabled(true);
                                        etmatricula.setEnabled(false);
                                        etcarnet.setEnabled(false);
                                        btconsultarCarnet.setEnabled(false);
                                        btconsultarMateria.setEnabled(false);
                                        btconsultarMatricula.setEnabled(false);
                                    }
                                }else {
                                    etcarnet.setEnabled(true);
                                    etfecha.setEnabled(true);
                                    etmateria.setEnabled(true);
                                    etmatricula.setEnabled(false);
                                    btadiccionar.setEnabled(true);
                                    btconsultarMateria.setEnabled(true);
                                    btconsultarCarnet.setEnabled(true);
                                    Toast.makeText(MatriculaActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                    etfecha.requestFocus();
                                }


                            }else {
                                Toast.makeText(MatriculaActivity.this, "Documento no econtrado", Toast.LENGTH_SHORT).show();
                                etmatricula.requestFocus();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Codigo de la matricula es requerido", Toast.LENGTH_SHORT).show();
            etmatricula.requestFocus();
        }
    }//Fin metodo consultar Matricula

    public void ConsultarCarnet(View view) {
        Consultar_Carnet();
    }

    //Metodo para consultar el carnet del estudiante
    private void Consultar_Carnet(){
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
                                        tvnombre.setText(document.getString("Nombre"));
                                        etcarnet.setEnabled(false);
                                    }
                                }else {

                                    Toast.makeText(MatriculaActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                }

                                etcarnet.setEnabled(true);

                            }else {
                                Toast.makeText(MatriculaActivity.this, "Documento no econtrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Carnet requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo consultar Carnet del estudiante

    public void ConsultarMateria(View view){
        Consultar_materia();
    }

    //Metodo para consultar el codigo de la materia
    private void Consultar_materia(){
        codigo_materia= etmateria.getText().toString();
        if (!codigo_materia.isEmpty()){
            db.collection(materias)
                    .whereEqualTo("Codigo_Materia",codigo_materia)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave = document.getId(); //Aqui tomamos el valos hash del documento
                                        tvmateria.setText(document.getString("Materia"));
                                        etmateria.setEnabled(false);
                                    }
                                }else {

                                    Toast.makeText(MatriculaActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                }
                                etmateria.setEnabled(true);

                            }else {
                                Toast.makeText(MatriculaActivity.this, "Documento no econtrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Codigo de la materia", Toast.LENGTH_SHORT).show();
            etmateria.requestFocus();
        }
    }//Fin metodo consultar codigo de la materia

    //Boton Regresar
    public void Regresar(View view){

        Intent intent = new Intent(MatriculaActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void Limpiar(View view){
        LimpiarCampos();
    }
    private void LimpiarCampos(){
        etmatricula.setText("");
        etmatricula.setEnabled(true);
        etmateria.setEnabled(false);
        etfecha.setText("");
        etfecha.setEnabled(false);
        etcarnet.setText("");
        cbactivo.setChecked(false);
        tvnombre.setText("");
        etcarnet.setEnabled(false);;
        tvmateria.setText("");
        etmateria.setText("");
        btadiccionar.setEnabled(false);
        btanular.setEnabled(false);
        cbactivo.setEnabled(false);
        etmatricula.requestFocus();
        btconsultarMatricula.setEnabled(true);


    }//Fin limpiar campos
}