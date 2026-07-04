package dao.instancia;
import java.util.List;

public interface InvolucradoDAO {
    void agregarInvolucrado(int idInstancia, String involucrado);
    void eliminarInvolucrado(int idInstancia, String involucrado);
    void eliminarPorIncidencia(int idInstancia);
    List<String> obtenerPorIncidencia(int idInstancia);
}