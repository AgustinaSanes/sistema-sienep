package modelos.instancia;
import modelos.usuario.Estudiante;
import modelos.usuario.Funcionario;
import prototype.Clonar;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

//Clase Incidencia
public class Incidencia extends Instancia{
    private List<String> involucrados;
    private String lugar;

    //Constructores
    public Incidencia(){super();}

    public Incidencia(int id, boolean comConfidencial, String titulo, LocalDateTime fechaHora, String comentario,boolean estado,Estudiante estudiante, Funcionario funcionario, String lugar){
        super(id,comConfidencial,titulo,fechaHora,comentario,estado,estudiante,funcionario);
        this.involucrados=new ArrayList<>();
        this.lugar=lugar;
    }

    //Getters
    public List<String> getInvolucrados(){return involucrados;}
    public String getLugar(){return lugar;}
    //Setters
    public void setLugar(String lugar){this.lugar=lugar;}
    public void agregarInvolucrado(String involucrado){this.involucrados.add(involucrado);}

    @Override
    public Clonar clonar() {
        return new Incidencia(
                0,
                this.comConfidencial,
                this.titulo + " (copia)",
                LocalDateTime.now(),
                this.comentario,
                this.estado,
                this.estudiante,
                this.funcionario,
                this.lugar
        );
    }
}
