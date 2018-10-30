package com.app.usuario.kiosco;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    EditText editTextNombre;
    Button buttonAddArtist;
    ListView listViewProductos;

    //a list to store all the artist from firebase database
    List<Producto> productos;
    private EditText editTextFilter;
    //our database reference object
    DatabaseReference databaseProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_window);

        //getting the reference of artists node
        databaseProductos = FirebaseDatabase.getInstance().getReference("Productos");
        listViewProductos = (ListView) findViewById(R.id.listViewProductos);

        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);

        //list to store artists
        productos = new ArrayList<>();


        listViewProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Producto producto = productos.get(i);
                showUpdateDeleteDialog(producto.getId(), producto.getNombre());
                return true;
            }
        });

        //adding an onclicklistener to button
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProducto();
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
                //attaching adapter to the listview
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

    private void showUpdateDeleteDialog(final String id, String prodName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        dialogBuilder.setTitle(prodName);
        final AlertDialog b = dialogBuilder.create();
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
                        updateProducto(id, text, precio);
                        b.dismiss();
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

    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addProducto() {
        EditText editNombre = (EditText) findViewById(R.id.editTextName);
        String name = editNombre.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            EditText editP = (EditText) findViewById(R.id.editTextPrecio);
            String ageText = editP.getText().toString();
            int precio = 0;
            if(! TextUtils.isEmpty(ageText)){
                precio = Integer.parseInt(ageText);
                String id = databaseProductos.push().getKey();
                Producto producto = new Producto(id, name, precio);
                databaseProductos.child(id).setValue(producto);
                editNombre.setText("");
                editP.setText("");
                Toast.makeText(this, "Producto Creado.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Escribe un Precio", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Escribe un Nombre", Toast.LENGTH_LONG).show();
        }
    }
}
