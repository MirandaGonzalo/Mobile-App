package com.app.usuario.kiosco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PRODUCTO_NAME = "net.simplifiedcoding.firebasedatabaseexample.productonombre";
    public static final String PRODUCTO_PRECIO = "net.simplifiedcoding.firebasedatabaseexample.productoprecio";
    Button Button11;
    ListView listViewProductos;
    List<Producto> productos;
    DatabaseReference databaseProductos;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseProductos = FirebaseDatabase.getInstance().getReference("Productos");
        listViewProductos = (ListView) findViewById(R.id.lista);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
        productos = new ArrayList<>();
        Button11 = (Button) findViewById(R.id.btnLog_in);
        Button11.setBackgroundColor(getResources().getColor(R.color.red));
        listViewProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Producto producto = productos.get(i);
                return true;
            }
        });

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Producto producto = productos.get(i);
                Intent intent = new Intent(getApplicationContext(), ProductoList.class);
                String numberAsString = String.valueOf(producto.getPrecio());
                intent.putExtra(PRODUCTO_PRECIO, numberAsString);
                intent.putExtra(PRODUCTO_NAME, producto.getNombre());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        databaseProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Producto producto = postSnapshot.getValue(Producto.class);
                    productos.add(producto);
                }
                ProductoList productoAdapter = new ProductoList(MainActivity.this, productos);
                progressBar.setVisibility(View.GONE);
                listViewProductos.setAdapter(productoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i=new Intent(MainActivity.this, second_window.class);
            startActivity(i);
        }
    }

    public void log_In(View view){
        Intent i=new Intent(MainActivity.this, log_in.class);
        startActivity(i);
    }
}
