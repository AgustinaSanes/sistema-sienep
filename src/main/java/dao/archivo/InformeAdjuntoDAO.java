package dao.archivo;
import modelos.archivo.InformeAdjunto;
import java.util.*;

public interface InformeAdjuntoDAO {
    void agregarArchivo(InformeAdjunto informe);
    void actualizarArchivo(InformeAdjunto informe);
    void eliminarArchivo(int id);
    InformeAdjunto obtenerPorId(int id);
    List<InformeAdjunto> obtenerPorEstudiante(String cedula);
    List<InformeAdjunto> obtenerTodos();
}
