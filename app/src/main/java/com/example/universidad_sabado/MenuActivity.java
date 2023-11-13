package com.example.universidad_sabado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Button btestudiantes, btmaterias, exitButton; // Agrega el botón "Salir"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Asociar los objetos java con los objetos XML
        btestudiantes = findViewById(R.id.btestudiantes);
        btmaterias = findViewById(R.id.btmaterias);
        exitButton = findViewById(R.id.btsalir);

        // Agrega un OnClickListener para el botón "Salir"
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });
    }


    public void Estudiantes(View view) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void Materias(View view) {
        Intent intent = new Intent(MenuActivity.this, MateriasActivity2.class);
        startActivity(intent);
    }

     public void Matriculas(View view){
        Intent intent = new Intent(MenuActivity.this, MatriculaActivity.class);
        startActivity(intent);
            }

    public void Salir(View view){

        showExitDialog();
    }
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas salir?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MenuActivity.this, loginActivity2.class);
                startActivity(intent);
                finish(); // Cierra la aplicación
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada, simplemente cierra el diálogo
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
