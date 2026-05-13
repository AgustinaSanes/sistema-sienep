package dao.recordatorio;
import modelos.recordatorio.Recordatorio;
import java.util.List;

public interface RecordatorioDAO {
    void agregarRecordatorio(Recordatorio recordatorio);
    void actualizarRecordatorio(Recordatorio recordatorio);
    void eliminarRecordatorio(int id);
    Recordatorio obtenerPorId(int id);
    List<Recordatorio> obtenerPorInstancia(int idInstancia);
}