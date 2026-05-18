package servicios.usuario;
import dao.usuario.RolDAO;
import dao.usuario.RolDAOImpl;
import modelos.usuario.Rol;
import java.util.List;

public class RolService {

    private final RolDAO rolDAO;

    public RolService() {
        this.rolDAO = new RolDAOImpl();
    }

    //Validar rol
    private void validarRol(Rol rol) {

        if (rol == null) {
            throw new RuntimeException("El rol no puede ser nulo");
        }

        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }

        if (rol.getNombre().length() > 50) {
            throw new RuntimeException("El nombre del rol no puede superar 50 caracteres");
        }
    }

    //Agregar rol
    public void agregarRol(Rol rol) {

        validarRol(rol);

        //Control de duplicados
        List<Rol> roles = rolDAO.obtenerTodos();
        for (Rol r : roles) {
            if (r.getNombre().equalsIgnoreCase(rol.getNombre())) {
                throw new RuntimeException("Ya existe un rol con ese nombre");
            }
        }

        rolDAO.agregarRol(rol);
    }

    //Actualizar rol
    public void actualizarRol(Rol rol) {

        validarRol(rol);

        if (rolDAO.obtenerPorId(rol.getId()) == null) {
            throw new RuntimeException("El rol no existe");
        }

        List<Rol> roles = rolDAO.obtenerTodos();

        for (Rol r : roles) {

            if (r.getNombre().equalsIgnoreCase(rol.getNombre())
                    && r.getId() != rol.getId()) {

                throw new RuntimeException(
                        "Ya existe un rol con ese nombre"
                );
            }
        }

        rolDAO.actualizarRol(rol);
    }

    //Eliminar rol
    public void eliminarRol(int id) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        if (rolDAO.obtenerPorId(id) == null) {
            throw new RuntimeException("El rol no existe");
        }

        rolDAO.eliminarRol(id);
    }

    //Consultar rol por id
    public Rol obtenerPorId(int id) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        return rolDAO.obtenerPorId(id);
    }

    //Obtener todos los roles
    public List<Rol> obtenerTodos() {
        return rolDAO.obtenerTodos();
    }
}