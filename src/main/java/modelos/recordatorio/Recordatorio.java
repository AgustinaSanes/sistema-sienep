package modelos.recordatorio;
import java.time.LocalDateTime;

//Clase Recordatorio
public class Recordatorio {
    private int id;
    private String titulo;
    private LocalDateTime fechaHora;
    private String tipo;
    private boolean estado;
    private Frecuencia frecuencia;

    //Constructores
    public Recordatorio() {}

    public Recordatorio(int id,String titulo,LocalDateTime fechaHora,String tipo,boolean estado,Frecuencia frecuencia){
        this.id=id;
        this.titulo=titulo;
        this.fechaHora=fechaHora;
        this.tipo=tipo;
        this.estado=estado;
        this.frecuencia=frecuencia;
    }

    //Getters
    public int getId(){return id;}
    public String getTitulo(){return titulo;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public String getTipo(){return tipo;}
    public boolean isEstado(){return estado;}
    public Frecuencia getFrecuencia(){return frecuencia;}

    //Setters
    public void setId(int id){this.id=id;}
    public void setTitulo(String titulo){this.titulo=titulo;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora=fechaHora;}
    public void setTipo(String tipo){this.tipo=tipo;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void setFrecuencia(Frecuencia frecuencia){this.frecuencia=frecuencia;}

    @Override
    public String toString() {
        return "ID: " + id +
                " | Titulo: " + titulo +
                " | FechaHora: " + fechaHora +
                " | Tipo: " + tipo +
                " | Estado: " + estado +
                " | Frecuencia: " + frecuencia;
    }
}
