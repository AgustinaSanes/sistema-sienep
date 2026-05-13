package modelos.instancia;

public class Categoria {
    private int id;
    private String nombre;
    private boolean estado;

    public Categoria(int id,String nombre,boolean estado){
        this.id=id;
        this.nombre=nombre;
        this.estado=estado;
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
