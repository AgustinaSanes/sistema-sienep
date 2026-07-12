package servicios.institucion;
import dao.institucion.*;
import modelos.institucion.ITR;
import util.ValidarTelefono;

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
        for (ITR existente : itrDAO.obtenerTodos()) {
            if (existente.getId() != itr.getId()
                    && existente.getNombre().equalsIgnoreCase(itr.getNombre())) {
                throw new RuntimeException("Ya existe un ITR con ese nombre");
            }
        }
        if (itr.getCalle() == null || itr.getCalle().trim().isEmpty()) {
            throw new RuntimeException("La calle es obligatoria");
        }
        if (itr.getCalle().length() > 50) {
            throw new RuntimeException("La calle no puede superar los 50 caracteres");
        }
        if (itr.getNroPuerta() == null || itr.getNroPuerta().trim().isEmpty()) {
            throw new RuntimeException("El número de puerta es obligatorio");
        }
        if (itr.getNroPuerta().length() > 50) {
            throw new RuntimeException("El número de puerta no puede superar los 50 caracteres");
        }
        if (itr.getCiudad() == null || itr.getCiudad().trim().isEmpty()) {
            throw new RuntimeException("La ciudad es obligatoria");
        }
        if (itr.getCiudad().length() > 100) {
            throw new RuntimeException("La ciudad no puede superar los 100 caracteres");
        }
        if (itr.getDepartamento() == null || itr.getDepartamento().trim().isEmpty()) {
            throw new RuntimeException("El departamento es obligatorio");
        }
        if (itr.getDepartamento().length() > 100) {
            throw new RuntimeException("El departamento no puede superar los 100 caracteres");
        }
        ValidarTelefono.validar(itr.getTelefono());
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
        ITR existente = itrDAO.obtenerPorId(id);
        if (existente == null) {
            throw new RuntimeException("El ITR no existe");
        }
        if (!existente.isEstado()) {
            throw new RuntimeException("El ITR ya se encuentra dado de baja");
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
        if (itrDAO.obtenerPorId(idItr) == null) {
            throw new RuntimeException("El ITR no existe");
        }
        itrCarreraDAO.agregarCarreraITR(idItr, idCarrera);
    }

    public void eliminarCarreraITR(int idItr, int idCarrera) {
        itrCarreraDAO.eliminarCarreraITR(idItr, idCarrera);
    }

    public List<Integer> obtenerCarrerasPorITR(int idItr) {
        return itrCarreraDAO.obtenerCarrerasPorITR(idItr);
    }

    public List<Integer> obtenerITRsPorCarrera(int idCarrera) {
        return itrCarreraDAO.obtenerITRsPorCarrera(idCarrera);
    }
}