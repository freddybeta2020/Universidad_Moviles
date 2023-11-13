package com.example.universidad_sabado;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;

public class loginActivity2 extends AppCompatActivity {
     EditText etEmail, etContraseña;
     Button btnEnviar;

    private FirebaseAuth mAuth;

    String email, contraseña;
    private static final String TAG = "LoginActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etemail);
        etContraseña = findViewById(R.id.etcontraseña);
        btnEnviar = findViewById(R.id.btenviar);
    }

    public void  Enviar(View view) {
         email = etEmail.getText().toString();
        contraseña = etContraseña.getText().toString();

        if (email.isEmpty()|| contraseña.isEmpty()) {
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        }else{

           mAuth.signInWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(loginActivity2.this, MenuActivity.class);
                            startActivity(intent);
                            Limpiar();
                        } else {
                            // Inicio de sesión fallido
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity2.this, "Autenticación fallida Datos incorrectos",
                                    Toast.LENGTH_SHORT).show();
                            etEmail.requestFocus();
                            Limpiar();
                        }
                    }
                });
           }

        }

        public void Limpiar(){
        LimpiarCampos();
        }

        //Funcion limpiar campos

       private void LimpiarCampos(){
        etEmail.setText("");
        etContraseña.setText("");
        etEmail.requestFocus();
       }
    }