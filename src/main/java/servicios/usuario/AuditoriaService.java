package servicios.usuario;
import dao.auditoria.AuditoriaDAO;
import dao.auditoria.AuditoriaDAOImpl;
import modelos.auditoria.Auditoria;

import java.time.LocalDate;
import java.util.List;

public class AuditoriaService {

    private final AuditoriaDAO auditoriaDAO;

    public AuditoriaService() {
        this.auditoriaDAO = new AuditoriaDAOImpl();
    }

    // Registrar una acción en auditoría
    public void registrar(String cedula, String accion, String detalle, String entidadAfectada) {
        Auditoria auditoria = new Auditoria(cedula, accion, detalle, entidadAfectada);
        auditoriaDAO.registrar(auditoria);
    }

    // Obtener todas las auditorías
    public List<Auditoria> obtenerTodas() {
        return auditoriaDAO.obtenerTodas();
    }

    // Obtener auditorías de un usuario específico
    public List<Auditoria> obtenerPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new RuntimeException("Cédula inválida");
        }
        return auditoriaDAO.obtenerPorCedula(cedula);
    }
    // Obtener auditorías por fecha
    public List<Auditoria> obtenerPorFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("Fecha inválida");
        }
        return auditoriaDAO.obtenerPorFecha(fecha);
    }
}