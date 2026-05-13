package dao.usuario;
import modelos.usuario.Rol;
import java.util.List;

public interface RolDAO {
    void agregarRol(Rol rol);
    void actualizarRol(Rol rol);
    void eliminarRol(int id);
    Rol obtenerPorId(int id);
    List<Rol> obtenerTodos();
}

