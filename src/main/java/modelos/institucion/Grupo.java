package modelos.institucion;
//Clase Grupo
public class Grupo {
    private int id;
    private String nombre;
    private boolean estado;
    private Carrera carrera;

    //Constructor
    public Grupo(){}

    public Grupo(int id,String nombre,boolean estado,Carrera carrera){
        this.id=id;
        this.nombre=nombre;
        this.estado=estado;
        this.carrera=carrera;
    }
    //Getters
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public boolean isEstado(){return estado;}
    public Carrera getCarrera(){return carrera;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void setCarrera(Carrera carrera){this.carrera=carrera;}
}
