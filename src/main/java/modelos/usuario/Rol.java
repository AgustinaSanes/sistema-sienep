package modelos.usuario;
import java.util.List;
import java.util.ArrayList;

//Clase Rol
public class Rol {
    private int id;
    private String nombre;
    private boolean estado;

    //Relaciones(N:N)
    private List<Permiso> permisos;

    //Constructor
    public Rol() {
    }

    public Rol(int id, String nombre,boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado=estado;
        this.permisos = new ArrayList<>();
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEstado(){
        return estado;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(boolean estado){
        this.estado=estado;
    }

    //metodo
    public void agregarPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre;
    }
}

