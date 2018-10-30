package com.app.usuario.kiosco;

public class Producto {

    String id;
    String nombre;
    Integer precio;


    public Producto (){

    }

    public Producto(String id, String nombre, Integer precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreciofromString(Producto producto){
        String numberAsString = String.valueOf(producto.getPrecio());
        String precio = " $ " + numberAsString;
        return precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "precio=" + precio +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
