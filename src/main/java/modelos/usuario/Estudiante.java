package modelos.usuario;
import modelos.archivo.InformeAdjunto;
import modelos.institucion.Grupo;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

//Clase Estudiante hereda de Usuario
public class Estudiante extends Usuario {
    private String foto;
    private String sistemaSalud;
    private LocalDate fechaNacimiento;
    private List<String> telefono;
    private String calle;
    private String nroPuerta;
    private boolean obsConfidenciales;
    private String motivo;
    private String obsComentarios;
    private String infoEstadoSalud;
    //Relacion a informe(1:N)
    private List<InformeAdjunto> informes;
    //Relacion a grupo (N:1)
    private Grupo grupo;

    //Constructores
    public Estudiante(){
        super();
    }

    public Estudiante(String cedula, String nombre, String apellido, String email, String contrasena, boolean estado, Rol rol, String foto,String sistemaSalud,LocalDate fechaNacimiento, String calle, String nroPuerta, boolean obsConfidenciales,String motivo,String obsComentarios,String infoEstadoSalud){
        super(cedula,nombre,apellido,email,contrasena,estado,rol);
        this.foto=foto;
        this.sistemaSalud=sistemaSalud;
        this.fechaNacimiento=fechaNacimiento;
        this.telefono=new ArrayList<>();
        this.calle=calle;
        this.nroPuerta=nroPuerta;
        this.obsConfidenciales=obsConfidenciales;
        this.motivo=motivo;
        this.obsComentarios=obsComentarios;
        this.infoEstadoSalud=infoEstadoSalud;
        this.informes=new ArrayList<>();
    }

    //Getters
        public String getFoto(){return foto;}
        public String getSistemaSalud(){return sistemaSalud;}
        public LocalDate getFechaNacimiento(){return fechaNacimiento;}
        public List<String> getTelefono(){return telefono;}
        public String getCalle(){return calle;}
        public String getNroPuerta(){return nroPuerta;}
        public boolean isObsConfidenciales(){return obsConfidenciales;}
        public String getMotivo(){return motivo;}
        public String getObsComentarios(){return obsComentarios;}
        public String getInfoEstadoSalud(){return infoEstadoSalud;}
        public List<InformeAdjunto> getInformes(){return informes;}
        public Grupo getGrupo(){return grupo;}

    //Setters
        public void setFoto(String foto){this.foto=foto;}
        public void setSistemaSalud(String sistemaSalud){this.sistemaSalud=sistemaSalud;}
        public void setFechaNacimiento(LocalDate fechaNacimiento){this.fechaNacimiento=fechaNacimiento;}
        public void agregarTelefono(String telefono){this.telefono.add(telefono);}
        public void setCalle(String calle){this.calle=calle;}
        public void setNroPuerta(String nroPuerta){this.nroPuerta=nroPuerta;}
        public void setObsConfidenciales(boolean obsConfidenciales){this.obsConfidenciales=obsConfidenciales;}
        public void setMotivo(String motivo){this.motivo=motivo;}
        public void setObsComentarios(String obsComentarios){this.obsComentarios=obsComentarios;}
        public void setInfoEstadoSalud(String infoEstadoSalud){this.infoEstadoSalud=infoEstadoSalud;}
        public void agregarInforme(InformeAdjunto informe){this.informes.add(informe);}
        public void setGrupo(Grupo grupo){this.grupo=grupo;}

    @Override
    public String toString() {
        return super.toString() +
                " | Sistema Salud: " + sistemaSalud +
                " | Fecha Nacimiento: " + fechaNacimiento +
                " | Calle: " + calle +
                " | Nro Puerta: " + nroPuerta +
                " | Grupo: " + (grupo != null ? grupo : "Sin grupo");
    }
}
