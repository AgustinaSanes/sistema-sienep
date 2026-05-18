package modelos.recordatorio;

//Clase Frecuencia
public class Frecuencia {
    private int id;
    private String descripcion;

    //Constructores
    public Frecuencia() {}

    public Frecuencia(int id,String descripcion){
        this.id=id;
        this.descripcion=descripcion;
    }

    //Getters
    public int getId(){return id;}
    public String getDescripcion(){return descripcion;}

    //Setters
    public void setId(int id){this.id=id;}
    public void setDescripcion(String descripcion){this.descripcion=descripcion;}

    @Override
    public String toString() {
        return "ID: " + id +
                " | Descripcion: " + descripcion;
    }
}
