package com.app.usuario.kiosco;



import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

        import java.util.List;


public class ProductoList extends ArrayAdapter<Producto> {
    private Activity context;
    List<Producto> productos;

    public ProductoList(Activity context, List<Producto> productos) {
        super(context, R.layout.layout_producto_list, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_producto_list, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewNombre);
        TextView textViewPrecio = (TextView) listViewItem.findViewById(R.id.textViewPrecio);
        Producto producto = productos.get(position);
        textViewName.setText(producto.getNombre());
        textViewPrecio.setText(producto.getPreciofromString(producto));
        return listViewItem;
    }

    }


