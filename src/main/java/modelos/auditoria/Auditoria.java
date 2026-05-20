package modelos.auditoria;
import java.time.LocalDateTime;

public class Auditoria {
    private int id;
    private String cedula;
    private String accion;
    private String detalle;
    private LocalDateTime fechaHora;
    private String entidadAfectada;

    public Auditoria(String cedula,String accion,String detalle,String entidadAfectada){
        this.cedula=cedula;
        this.accion=accion;
        this.detalle=detalle;
        this.entidadAfectada=entidadAfectada;
        this.fechaHora = LocalDateTime.now();
    }
    //Getters
    public int getId(){return id;}
    public String getCedula(){return cedula;}
    public String getAccion(){return accion;}
    public String getDetalle(){return detalle;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public String getEntidadAfectada(){return entidadAfectada;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setCedula(String cedula){this.cedula=cedula;}
    public void setAccion(String accion){this.accion=accion;}
    public void setDetalle(String detalle){this.detalle=detalle;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora=fechaHora;}
    public void setEntidadAfectada(String entidadAfectada){this.entidadAfectada=entidadAfectada;}
}
