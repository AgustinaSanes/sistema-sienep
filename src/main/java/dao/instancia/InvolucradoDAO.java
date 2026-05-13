package dao.instancia;
import java.util.List;

public interface InvolucradoDAO {
    void agregarInvolucrado(int idIncidencia, String involucrado);
    void eliminarPorIncidencia(int idIncidencia);
    List<String> obtenerPorIncidencia(int idIncidencia);
}