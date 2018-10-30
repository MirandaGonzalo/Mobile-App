package com.app.usuario.kiosco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class log_in extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correnom;
    private EditText pass;
    private FirebaseAuth.AuthStateListener Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        Button registra = (Button) findViewById(R.id.registra);

        mAuth = FirebaseAuth.getInstance();


        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                }
            }
        };
    }


    public void registrausuario(View view){
        EditText correnom = (EditText) findViewById(R.id.correnom);
        EditText pass = (EditText) findViewById(R.id.pass);
        String email = correnom.getText().toString();
        String passw = pass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i=new Intent(log_in.this, second_window.class);
                    startActivity(i);
                }
            }
        });
    }

}