package dao.usuario;
import modelos.usuario.Permiso;
import java.util.List;

public interface RolPermisoDAO {
    void asignarPermiso(int idRol,int idPermiso);
    void quitarPermiso(int idRol, int idPermiso);
    List<Permiso> obtenerPermisosRol(int idRol);
}