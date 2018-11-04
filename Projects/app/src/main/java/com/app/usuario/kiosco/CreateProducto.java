package com.app.usuario.kiosco;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CreateProducto extends ArrayAdapter<CreateProducto> {
    private Activity context;

    public CreateProducto(Activity context) {
        super(context, R.layout.create_dialog);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.create_dialog, null, true);
        return listViewItem;
    }

}