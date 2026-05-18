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

    //Constructores
    public Rol() {}

    public Rol(int id, String nombre,boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado=estado;
        this.permisos = new ArrayList<>();
    }

    //Roles predefinidos
    public static List<Rol> cargarRolesBase(){

        List<Rol> roles = new ArrayList<>();

        roles.add(new Rol(1, "Administrador", true));
        roles.add(new Rol(2, "Analista de Equipo Educativo", true));
        roles.add(new Rol(3, "Psicopedagogo/a", true));
        roles.add(new Rol(4, "Estudiante", true));
        roles.add(new Rol(5, "Funcionario UTEC", true));

        return roles;
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
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setEstado(boolean estado){
        this.estado=estado;
    }
    public void agregarPermiso(Permiso permiso) {this.permisos.add(permiso);}

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nombre: " + nombre +
                " | Estado: " + estado +
                " | Permisos: " + permisos;
    }
}

