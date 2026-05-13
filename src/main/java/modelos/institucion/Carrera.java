package modelos.institucion;
import java.util.List;
import java.util.ArrayList;

//Clase Carrera
public class Carrera {
    private int id;
    private String nombre;
    private boolean estado;
    private List<ITR> itrs;
    private List<Grupo> grupos;

    //Constructor
    public Carrera(int id,String nombre,boolean estado){
        this.id=id;
        this.nombre=nombre;
        this.estado=estado;
        this.itrs=new ArrayList<>();
        this.grupos=new ArrayList<>();
    }
    public Carrera(){
    }

    //Getters
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public boolean isEstado(){return estado;}
    public List<ITR> getItrs(){return itrs;}
    public List<Grupo> getGrupos(){return grupos;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void agregarGrupo(Grupo grupo){this.grupos.add(grupo);}
    public void agregarITR(ITR itr){this.itrs.add(itr);}
}
