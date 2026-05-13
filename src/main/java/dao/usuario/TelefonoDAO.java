package dao.usuario;
import java.util.List;

public interface TelefonoDAO {
    void agregarTelefono(String cedula, String numero);
    void eliminarTelefono(int id);
    List<String> obtenerPorEstudiante(String cedula);
}