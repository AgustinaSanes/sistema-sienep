package dao.institucion;
import java.util.List;

public interface ITRCarreraDAO {
    void agregarCarreraITR(int idItr, int idCarrera);
    void eliminarCarreraITR(int idItr, int idCarrera);
    List<Integer> obtenerCarrerasPorITR(int idItr);
    List<Integer> obtenerITRsPorCarrera(int idCarrera);
}