package dao.informe;
import modelos.informe.InformeAdjunto;
import java.util.*;

public interface InformeAdjuntoDAO {
    void agregarInforme(InformeAdjunto informe);
    void actualizarInforme(InformeAdjunto informe);
    void eliminarInforme(int id);
    InformeAdjunto obtenerPorId(int id);
    List<InformeAdjunto> obtenerPorEstudiante(String cedula);
    List<InformeAdjunto> obtenerTodos();
}
