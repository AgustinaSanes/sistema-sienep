package servicios.usuario;

import dao.usuario.PermisoDAO;
import dao.usuario.PermisoDAOImpl;
import modelos.usuario.Permiso;

import java.util.List;

public class PermisoService {

    private PermisoDAO permisoDAO;

    public PermisoService() {
        this.permisoDAO = new PermisoDAOImpl();
    }

    // Validar permiso
    private void validarPermiso(Permiso permiso) {

        if (permiso == null) {
            throw new RuntimeException("El permiso no puede ser nulo");
        }

        if (permiso.getDescripcion() == null || permiso.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción del permiso es obligatoria");
        }

        if (permiso.getDescripcion().length() > 255) {
            throw new RuntimeException("La descripción no puede superar los 255 caracteres");
        }
    }

    // Agregar permiso
    public void agregarPermiso(Permiso permiso) {

        validarPermiso(permiso);

        // Evitar duplicados
        List<Permiso> permisos = permisoDAO.obtenerTodos();

        for (Permiso p : permisos) {
            if (p.getDescripcion().equalsIgnoreCase(permiso.getDescripcion())) {
                throw new RuntimeException("Ya existe un permiso con esa descripción");
            }
        }

        permisoDAO.agregarPermiso(permiso);
    }

    // Actualizar permiso
    public void actualizarPermiso(Permiso permiso) {

        validarPermiso(permiso);

        if (permiso.getId() <= 0) {
            throw new RuntimeException("ID inválido");
        }

        if (permisoDAO.obtenerPorId(permiso.getId()) == null) {
            throw new RuntimeException("El permiso no existe");
        }

        // Control de duplicados
        List<Permiso> permisos = permisoDAO.obtenerTodos();

        for (Permiso p : permisos) {
            if (p.getDescripcion().equalsIgnoreCase(permiso.getDescripcion())
                    && p.getId() != permiso.getId()) {
                throw new RuntimeException("Ya existe un permiso con esa descripción");
            }
        }

        permisoDAO.actualizarPermiso(permiso);
    }

    // Eliminar permiso
    public void eliminarPermiso(int id) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        if (permisoDAO.obtenerPorId(id) == null) {
            throw new RuntimeException("El permiso no existe");
        }

        permisoDAO.eliminarPermiso(id);
    }

    // Consultar permiso por id
    public Permiso obtenerPorId(int id) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        return permisoDAO.obtenerPorId(id);
    }

    // Obtener todos los permisos
    public List<Permiso> obtenerTodos() {
        return permisoDAO.obtenerTodos();
    }
}