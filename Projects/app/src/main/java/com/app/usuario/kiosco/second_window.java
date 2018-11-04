package com.app.usuario.kiosco;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class second_window extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String PRODUCTO_NAME = "net.simplifiedcoding.firebasedatabaseexample.productonombre";
    public static final String PRODUCTO_PRECIO = "net.simplifiedcoding.firebasedatabaseexample.productoprecio";

    //view objects
    Button btn_layout_cargar, btn_crear, button;
    ListView listViewProductos;
    TextView user_title;
    private ArrayAdapter adapter;
    List<Producto> productos;
    private ProgressBar progressBar2;
    DatabaseReference databaseProductos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_window);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        user_title = (TextView) findViewById(R.id.user_title);
        user_title.setText("Hola " + email);
        databaseProductos = FirebaseDatabase.getInstance().getReference("Productos");
        listViewProductos = (ListView) findViewById(R.id.listViewProductos);
        btn_layout_cargar = (Button) findViewById(R.id.btn_layout_cargar);
        button = (Button) findViewById(R.id.button);
        button.setBackgroundColor(getResources().getColor(R.color.red));
        btn_layout_cargar.setBackgroundColor(getResources().getColor(R.color.green));
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        productos = new ArrayList<>();


        listViewProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Producto producto = productos.get(i);
                showUpdateDeleteDialog(producto.getId(), producto.getNombre(), producto.getPrecio());
                return true;
            }
        });

        //adding an onclicklistener to button
        btn_layout_cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderProducto();
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
        //attaching value event listener
        progressBar2.setVisibility(View.VISIBLE);
        databaseProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                productos.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Producto producto = postSnapshot.getValue(Producto.class);
                    //adding artist to the list
                    productos.add(producto);
                }

                //creating adapter
                ProductoList artistAdapter = new ProductoList(second_window.this, productos);
                progressBar2.setVisibility(View.GONE);
                listViewProductos.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean deleteProducto(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Productos").child(id);

        //removing artist
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Producto Eliminado", Toast.LENGTH_LONG).show();

        return true;
    }

    private boolean updateProducto(String id, String nombre, Integer precio) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Productos").child(id);
        Producto producto = new Producto(id, nombre, precio);
        dR.setValue(producto);
        Toast.makeText(getApplicationContext(), "Producto Actualizado!", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String id, String prodName, Integer prodPrecio) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        String titulo = "Datos de " + prodName;
        dialogBuilder.setTitle(titulo);
        final AlertDialog b = dialogBuilder.create();
        final EditText nombre_u = (EditText) dialogView.findViewById(R.id.editNombre);
        nombre_u.setText(prodName);
        final EditText precio_u = (EditText) dialogView.findViewById(R.id.editPrecio);
        String numberAsString = String.valueOf(prodPrecio);
        precio_u.setText(numberAsString);
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit=(EditText)b.findViewById(R.id.editNombre);
                String text=edit.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    EditText editP = (EditText) b.findViewById(R.id.editPrecio);
                    String ageText = editP.getText().toString();
                    int precio = 0;
                    if(! TextUtils.isEmpty(ageText)){
                        precio = Integer.parseInt(ageText);
                        if (precio >= 99999){
                            Toast.makeText(getApplicationContext(), "El precio no es valido, el maximo es $99999.", Toast.LENGTH_LONG).show();
                        } else {
                            updateProducto(id, text, precio);
                            b.dismiss();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Escribe un Precio", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Escribe un Nombre", Toast.LENGTH_LONG).show();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteProducto(id);
                        b.dismiss();
                    }
                });
            }
        });
    }

    private void builderProducto() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Crear Producto:");
        final AlertDialog c = dialogBuilder.create();
        c.show();
        btn_crear = (Button) c.findViewById(R.id.btn_crear);

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editNombre = (EditText) c.findViewById(R.id.create_nombre);
                String name = editNombre.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    EditText editP = (EditText) c.findViewById(R.id.create_precio);
                    String ageText = editP.getText().toString();
                    int precio = 0;
                    if(! TextUtils.isEmpty(ageText)){
                        precio = Integer.parseInt(ageText);
                        if (precio >= 99999){
                            Toast.makeText(getApplicationContext(), "El precio no es valido, el maximo es $99999.", Toast.LENGTH_LONG).show();
                        } else {
                            String id = databaseProductos.push().getKey();
                            Producto producto = new Producto(id, name, precio);
                            databaseProductos.child(id).setValue(producto);
                            editNombre.setText("");
                            editP.setText("");
                            Toast.makeText(getApplicationContext(), producto.getNombre() + " Creado.", Toast.LENGTH_LONG).show();
                            c.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Escribe un Precio !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Escribe un Nombre !", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent i=new Intent(second_window.this, MainActivity.class);
        startActivity(i);
    }
}
