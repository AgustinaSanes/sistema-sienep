package dao.usuario;
import modelos.usuario.Permiso;
import java.util.List;

public interface PermisoDAO {
    void agregarPermiso(Permiso permiso);
    void actualizarPermiso(Permiso permiso);
    void eliminarPermiso(int id);
    Permiso obtenerPorId(int id);
    List<Permiso> obtenerTodos();
}