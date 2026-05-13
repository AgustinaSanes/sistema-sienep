package servicios.institucion;
import dao.institucion.*;
import modelos.institucion.ITR;
import java.util.List;

public class ITRService {

    private final ITRDAO itrDAO;
    private final ITRCarreraDAO itrCarreraDAO;

    public ITRService() {
        this.itrDAO = new ITRDAOImpl();
        this.itrCarreraDAO = new ITRCarreraDAOImpl();
    }

    private void validarITR(ITR itr) {
        if (itr == null) {
            throw new RuntimeException("El ITR no puede ser nulo");
        }
        if (itr.getNombre() == null || itr.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (itr.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
        if (itr.getTelefono() == null || itr.getTelefono().trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }
    }

    public void agregarITR(ITR itr) {
        validarITR(itr);
        itrDAO.agregarITR(itr);
    }

    public void actualizarITR(ITR itr) {
        validarITR(itr);
        if (itrDAO.obtenerPorId(itr.getId()) == null) {
            throw new RuntimeException("El ITR no existe");
        }
        itrDAO.actualizarITR(itr);
    }

    public void eliminarITR(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        itrDAO.eliminarITR(id);
    }

    public ITR obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return itrDAO.obtenerPorId(id);
    }

    public List<ITR> obtenerTodos() {
        return itrDAO.obtenerTodos();
    }

    public void agregarCarreraITR(int idItr, int idCarrera) {
        itrCarreraDAO.agregarCarreraITR(idItr, idCarrera);
    }

    public void eliminarCarreraITR(int idItr, int idCarrera) {
        itrCarreraDAO.eliminarCarreraITR(idItr, idCarrera);
    }
}