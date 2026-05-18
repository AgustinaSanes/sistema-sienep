package modelos.usuario;

//Clase padre
public abstract class Usuario {
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String contrasena;
    protected boolean estado;
    //Relaciones
    protected Rol rol;

    //Constructores
    public Usuario() {}

    public Usuario(String cedula, String nombre, String apellido, String email, String contrasena,
                         boolean estado,Rol rol) {
        this.cedula=cedula;
        this.nombre=nombre;
        this.apellido=apellido;
        this.email=email;
        this.contrasena=contrasena;
        this.estado=estado;
        this.rol=rol;
    }

    //Getters
    public String getCedula(){return cedula;}
    public String getNombre(){return nombre;}
    public String getApellido(){return apellido;}
    public String getEmail(){return email;}
    public String getContrasena(){return contrasena;}
    public boolean isEstado(){return estado;}
    public Rol getRol(){return rol;}

    //Setters
    public void setCedula(String cedula){this.cedula=cedula;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setApellido(String apellido){this.apellido=apellido;}
    public void setEmail(String email){this.email=email;}
    public void setContrasena(String contrasena){this.contrasena=contrasena;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void setRol(Rol rol){this.rol=rol;}

    @Override
    public String toString() {
        return "Cedula: " + cedula +
                " | Nombre: " + nombre +
                " | Apellido: " + apellido +
                " | Email: " + email +
                " | Estado: " + estado +
                " | Rol: " + rol.getNombre();
    }
}