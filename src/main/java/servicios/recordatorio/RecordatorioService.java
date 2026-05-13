package servicios.recordatorio;
import dao.recordatorio.RecordatorioDAO;
import dao.recordatorio.RecordatorioDAOImpl;
import modelos.recordatorio.Recordatorio;
import java.util.List;

public class RecordatorioService {

    private final RecordatorioDAO recordatorioDAO;

    public RecordatorioService() {
        this.recordatorioDAO = new RecordatorioDAOImpl();
    }

    private void validarRecordatorio(Recordatorio recordatorio) {
        if (recordatorio == null) {
            throw new RuntimeException("El recordatorio no puede ser nulo");
        }
        if (recordatorio.getTitulo() == null || recordatorio.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título es obligatorio");
        }
        if (recordatorio.getTitulo().length() > 100) {
            throw new RuntimeException("El título no puede superar los 100 caracteres");
        }
        if (recordatorio.getFechaHora() == null) {
            throw new RuntimeException("La fecha y hora son obligatorias");
        }
        if (recordatorio.getFrecuencia() == null) {
            throw new RuntimeException("La frecuencia es obligatoria");
        }
        if (recordatorio.getTipo() == null || recordatorio.getTipo().trim().isEmpty()) {
            throw new RuntimeException("El tipo es obligatorio");
        }
    }

    public void agregarRecordatorio(Recordatorio recordatorio) {
        validarRecordatorio(recordatorio);
        recordatorioDAO.agregarRecordatorio(recordatorio);
    }

    public void actualizarRecordatorio(Recordatorio recordatorio) {
        validarRecordatorio(recordatorio);
        if (recordatorioDAO.obtenerPorId(recordatorio.getId()) == null) {
            throw new RuntimeException("El recordatorio no existe");
        }
        recordatorioDAO.actualizarRecordatorio(recordatorio);
    }

    public void eliminarRecordatorio(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        if (recordatorioDAO.obtenerPorId(id) == null) {
            throw new RuntimeException("El recordatorio no existe");
        }
        recordatorioDAO.eliminarRecordatorio(id);
    }

    public Recordatorio obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return recordatorioDAO.obtenerPorId(id);
    }

    public List<Recordatorio> obtenerPorInstancia(int idInstancia) {
        if (idInstancia <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return recordatorioDAO.obtenerPorInstancia(idInstancia);
    }
}