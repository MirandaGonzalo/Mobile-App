package com.app.usuario.kiosco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class log_in extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar3;
    private FirebaseAuth.AuthStateListener Listener;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        inputEmail = (EditText) findViewById(R.id.text_email);
        inputPassword = (EditText) findViewById(R.id.text_pass);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        button = (Button) findViewById(R.id.registra);
        button.setBackgroundColor(getResources().getColor(R.color.red));
        progressBar3.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
    }

    public void onClick(View v) {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Escriba una direcccion de Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Escriba una Contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar3.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(log_in.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar3.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    Toast.makeText(log_in.this, "Error!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(log_in.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}