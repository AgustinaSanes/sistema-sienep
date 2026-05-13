package modelos.archivo;
import modelos.usuario.Estudiante;

//Clase InformeAdjunto
public class InformeAdjunto {
    private int id;
    private String nombre;
    private String tipoArchivo;
    private String rutaArchivo;
    private String categoria;
    private boolean confidencial;
    private boolean estado;
    //Relaciones(N:1)
    private Estudiante estudiante;

    //Constructor
    public InformeAdjunto(int id,String nombre,String tipoArchivo,String rutaArchivo,String categoria,boolean confidencial,boolean estado,Estudiante estudiante){
        this.id=id;
        this.nombre=nombre;
        this.tipoArchivo=tipoArchivo;
        this.rutaArchivo=rutaArchivo;
        this.categoria=categoria;
        this.confidencial=confidencial;
        this.estado=estado;
        this.estudiante=estudiante;
    }

    //Getters
    public int getId(){return id;}
    public String getNombre(){return nombre;}
    public String getTipoArchivo(){return tipoArchivo;}
    public String getRutaArchivo(){return rutaArchivo;}
    public String getCategoria(){return categoria;}
    public boolean isConfidencial(){return confidencial;}
    public boolean isEstado(){return estado;}
    public Estudiante getEstudiante(){return estudiante;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setTipoArchivo(String tipoArchivo){this.tipoArchivo=tipoArchivo;}
    public void setRutaArchivo(String rutaArchivo){this.rutaArchivo=rutaArchivo;}
    public void setCategoria(String categoria){this.categoria=categoria;}
    public void setConfidencial(boolean confidencial){this.confidencial=confidencial;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void setEstudiante(Estudiante estudiante){this.estudiante=estudiante;}
}
