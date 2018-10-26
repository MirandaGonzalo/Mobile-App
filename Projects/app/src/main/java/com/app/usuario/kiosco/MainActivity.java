package com.app.usuario.kiosco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void verProductos(View view){
        Intent myIntent = new Intent(view.getContext(), second_window.class);
        startActivityForResult(myIntent, 0);
    }

    public void onSaludo(View view) {
        EditText val_nombre = (EditText)findViewById(R.id.editText);
        EditText val_precio = (EditText)findViewById(R.id.editText2);
        String nombre = val_nombre.getText().toString();
        Integer precio = Integer.parseInt(val_precio.getText().toString());
        DatabaseReference myRef = database.getReference("Productos");
        String id = myRef.push().getKey();
        Producto new_producto = new Producto(id, nombre, precio);
        myRef.child(id).setValue(new_producto);
        Toast.makeText(this, "Producto Creado con Exito! ", Toast.LENGTH_LONG).show();
        val_nombre.setText("");
        val_precio.setText("");
    }
}


