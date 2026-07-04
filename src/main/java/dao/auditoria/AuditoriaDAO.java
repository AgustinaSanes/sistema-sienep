package dao.auditoria;
import modelos.auditoria.Auditoria;

import java.time.LocalDate;
import java.util.List;

public interface AuditoriaDAO {
    void registrar(Auditoria auditoria);
    List<Auditoria> obtenerTodas();
    List<Auditoria> obtenerPorCedula(String cedula);
    List<Auditoria> obtenerPorFecha(LocalDate fecha);
}
