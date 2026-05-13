package modelos.usuario;

//Clase Permiso
public class Permiso {
    private int id;
    private String descripcion;
    private boolean estado;

    //Constructor
    public Permiso(){};
    public Permiso(int id,String descripcion,boolean estado){
        this.id=id;
        this.descripcion=descripcion;
        this.estado=estado;
    }
    //Getters
    public int getId(){return id;}
    public String getDescripcion(){return descripcion;}
    public boolean isEstado(){return estado;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setDescripcion(String descripcion){this.descripcion=descripcion;}
    public void setEstado(boolean estado){this.estado=estado;}
}
