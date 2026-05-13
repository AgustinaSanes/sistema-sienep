package modelos.institucion;
import java.util.List;
import java.util.ArrayList;
//Clase ITR
public class ITR {
    private int id;
    private String nombre;
    private String calle;
    private String nroPuerta;
    private String ciudad;
    private String departamento;
    private String telefono;
    private boolean estado;
    private List<Carrera> carreras;

    //Constructor
    public ITR(int id,String nombre,String calle,String nroPuerta,String ciudad,String departamento,String telefono,boolean estado){
        this.id=id;
        this.nombre=nombre;
        this.calle=calle;
        this.nroPuerta=nroPuerta;
        this.ciudad=ciudad;
        this.departamento=departamento;
        this.telefono=telefono;
        this.estado=estado;
        this.carreras=new ArrayList<>();
    }
    //Getters
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public String getCalle(){return calle;}
    public String getNroPuerta(){return nroPuerta;}
    public String getCiudad(){return ciudad;}
    public String getDepartamento(){return departamento;}
    public String getTelefono(){return telefono;}
    public boolean isEstado(){return estado;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setCalle(String calle){this.calle=calle;}
    public void setNroPuerta(String nroPuerta){this.nroPuerta=nroPuerta;}
    public void setCiudad(String ciudad){this.ciudad=ciudad;}
    public void setDepartamento(String departamento){this.departamento=departamento;}
    public void setTelefono(String telefono){this.telefono=telefono;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void agregarCarrera(Carrera carrera){
        this.carreras.add(carrera);
        carrera.getItrs().add(this);
    }
}
