package dao.usuario;
import modelos.auditoria.Auditoria;
import java.util.List;

public interface AuditoriaDAO {
    void registrar(Auditoria auditoria);
    List<Auditoria> obtenerTodas();
    List<Auditoria> obtenerPorCedula(String cedula);
}
