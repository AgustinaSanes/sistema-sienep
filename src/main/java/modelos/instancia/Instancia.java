package modelos.instancia;
import modelos.recordatorio.Recordatorio;
import modelos.usuario.Estudiante;
import modelos.usuario.Funcionario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public abstract class Instancia {
    protected int id;
    protected boolean comConfidencial;
    protected String titulo;
    protected LocalDateTime fechaHora;
    protected String comentario;
    protected boolean estado;
    //Relaciones
    protected Estudiante estudiante;
    protected Funcionario funcionario;
    protected List<Recordatorio> recordatorios;

    public Instancia(){}

    public Instancia(int id,boolean comConfidencial,String titulo, LocalDateTime fechaHora,String comentario,boolean estado,Estudiante estudiante, Funcionario funcionario){
        this.id=id;
        this.comConfidencial=comConfidencial;
        this.titulo=titulo;
        this.fechaHora=fechaHora;
        this.comentario=comentario;
        this.estado=estado;
        this.recordatorios=new ArrayList<>();
        this.estudiante=estudiante;
        this.funcionario=funcionario;
    }
    //Getters
    public int getId(){return id;}
    public boolean getComConfidencial(){return comConfidencial;}
    public String getTitulo(){return titulo;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public String getComentario(){return comentario;}
    public boolean isEstado(){return estado;}
    public Estudiante getEstudiante(){return estudiante;}
    public Funcionario getFuncionario(){return funcionario;}
    public List<Recordatorio> getRecordatorios(){return recordatorios;}
    //Setters
    public void setId(int id){this.id=id;}
    public void setComConfidencial(boolean comConfidencial){this.comConfidencial=comConfidencial;}
    public void setTitulo(String titulo){this.titulo=titulo;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora=fechaHora;}
    public void setComentario(String comentario){this.comentario=comentario;}
    public void setEstado(boolean estado){this.estado=estado;}
    public void setEstudiante(Estudiante estudiante){this.estudiante=estudiante;}
    public void setFuncionario(Funcionario funcionario){this.funcionario=funcionario;}
    public void agregarRecordatorio(Recordatorio recordatorio){this.recordatorios.add(recordatorio);}
}
