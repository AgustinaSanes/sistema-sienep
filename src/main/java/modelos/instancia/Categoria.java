package modelos.instancia;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private int id;
    private String nombre;
    private boolean estado;

    public Categoria(int id,String nombre,boolean estado){
        this.id=id;
        this.nombre=nombre;
        this.estado=estado;
    }

    //Categorías predefinidas
    public static List<Categoria> cargarCategoriasBase(){

        List<Categoria> categorias = new ArrayList<>();

        categorias.add(new Categoria(1, "Reunión", true));
        categorias.add(new Categoria(2, "Llamada", true));
        categorias.add(new Categoria(3, "Coordinación", true));
        categorias.add(new Categoria(4, "Informe", true));
        return categorias;
    }

    //Getters
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public boolean isEstado(){return estado;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setEstado(boolean estado){this.estado=estado;}
}
