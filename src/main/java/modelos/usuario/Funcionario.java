package modelos.usuario;
import java.util.List;
import java.util.ArrayList;
import modelos.instancia.Instancia;
import modelos.recordatorio.Recordatorio;

//Clase Funcionario hereda de Usuario
public class Funcionario extends Usuario{
    private List<Instancia> instancias;
    private List<Recordatorio> recordatorios;

    //Constructores
    public Funcionario(){
        super();
    }

    public Funcionario(String cedula,String nombre,String apellido,String email,String contrasena,boolean estado,Rol rol){
        super(cedula,nombre,apellido,email,contrasena,estado,rol);
        this.instancias=new ArrayList<>();
        this.recordatorios=new ArrayList<>();
    }

    //Getters
    public List<Instancia> getInstancias(){return instancias;}
    public List<Recordatorio> getRecordatorios(){return recordatorios;}

    //Metodos
    public void agregarInstancia(Instancia instancia){this.instancias.add(instancia);}
    public void agregarRecordatorio(Recordatorio recordatorio){this.recordatorios.add(recordatorio);}

    @Override
    public String toString() {
        return super.toString() +
                " | Instancias: " + instancias.size() +
                " | Recordatorios: " + recordatorios.size();
    }
}
