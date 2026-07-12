package dao.recordatorio;
import modelos.recordatorio.Frecuencia;
import java.util.List;

public interface FrecuenciaDAO {
    void agregarFrecuencia(Frecuencia frecuencia);
    void modificarFrecuencia(Frecuencia frecuencia);
    void eliminarFrecuencia(int id);
    Frecuencia obtenerPorId(int id);
    List<Frecuencia> obtenerTodas();
}