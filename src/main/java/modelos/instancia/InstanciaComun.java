package modelos.instancia;
import modelos.usuario.Estudiante;
import modelos.usuario.Funcionario;
import java.time.LocalDateTime;

public class InstanciaComun extends Instancia {
    private Categoria categoria;

    //Constructores
    public InstanciaComun(){super();}
    public InstanciaComun(int id, boolean comConfidencial, String titulo, LocalDateTime fechaHora, String comentario, boolean estado, Estudiante estudiante, Funcionario funcionario, Categoria categoria){
        super(id,comConfidencial,titulo,fechaHora,comentario,estado,estudiante,funcionario);
        this.categoria=categoria;
    }
    //Getters
    public Categoria getCategoria(){return categoria;}
    //Setters
    public void setCategoria(Categoria categoria){this.categoria=categoria;}
}
