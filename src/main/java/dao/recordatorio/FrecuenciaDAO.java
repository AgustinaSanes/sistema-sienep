package dao.recordatorio;
import modelos.recordatorio.Frecuencia;
import java.util.List;

public interface FrecuenciaDAO {
    void agregarFrecuencia(Frecuencia frecuencia);
    Frecuencia obtenerPorId(int id);
    List<Frecuencia> obtenerTodas();
}