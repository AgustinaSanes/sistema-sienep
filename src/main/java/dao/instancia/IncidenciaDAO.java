package dao.instancia;
import modelos.instancia.Incidencia;

public interface IncidenciaDAO {
    void agregarIncidencia(Incidencia incidencia);
    void actualizarIncidencia(Incidencia incidencia);
    Incidencia obtenerPorInstancia(int idInstancia);
}