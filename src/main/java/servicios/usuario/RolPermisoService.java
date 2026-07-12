package servicios.usuario;

import dao.usuario.*;
import modelos.usuario.Permiso;

import java.util.List;

public class RolPermisoService {
    private final RolPermisoDAO rolPermisoDAO;
    private final RolService rolService;
    private final PermisoService permisoService;

    public RolPermisoService() {
        this.rolPermisoDAO = new RolPermisoDAOImpl();
        this.rolService = new RolService();
        this.permisoService = new PermisoService();
    }

    // Asignar permiso a rol
    public void asignarPermiso(int idRol, int idPermiso) {

        validarIds(idRol, idPermiso);

        // Validar existencia del rol
        if (rolService.obtenerPorId(idRol) == null) {
            throw new RuntimeException("El rol no existe");
        }

        // Validar existencia del permiso
        if (permisoService.obtenerPorId(idPermiso) == null) {
            throw new RuntimeException("El permiso no existe");
        }

        // Evitar permisos repetidos
        List<Permiso> permisos =
                rolPermisoDAO.obtenerPermisosRol(idRol);

        for (Permiso p : permisos) {

            if (p.getId() == idPermiso) {
                throw new RuntimeException(
                        "El permiso ya está asignado al rol"
                );
            }
        }

        rolPermisoDAO.asignarPermiso(idRol, idPermiso);
    }

    // Quitar permiso
    public void quitarPermiso(int idRol, int idPermiso) {

        validarIds(idRol, idPermiso);

        if (rolService.obtenerPorId(idRol) == null) {
            throw new RuntimeException("El rol no existe");
        }

        if (permisoService.obtenerPorId(idPermiso) == null) {
            throw new RuntimeException("El permiso no existe");
        }

        rolPermisoDAO.quitarPermiso(idRol, idPermiso);
    }

    // Obtener permisos asociados a un rol
    public List<Permiso> obtenerPermisosRol(int idRol) {

        if (idRol <= 0) {
            throw new RuntimeException("ID de rol inválido");
        }

        if (rolService.obtenerPorId(idRol) == null) {
            throw new RuntimeException("El rol no existe");
        }

        return rolPermisoDAO.obtenerPermisosRol(idRol);
    }

    // Validación auxiliar
    private void validarIds(int idRol, int idPermiso) {

        if (idRol <= 0) {
            throw new RuntimeException(
                    "ID de rol inválido"
            );
        }

        if (idPermiso <= 0) {
            throw new RuntimeException(
                    "ID de permiso inválido"
            );
        }
    }
}