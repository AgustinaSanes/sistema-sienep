package servicios.institucion;
import dao.institucion.GrupoDAO;
import dao.institucion.GrupoDAOImpl;
import modelos.institucion.Grupo;
import java.util.List;

public class GrupoService {

    private final GrupoDAO grupoDAO;

    public GrupoService() {
        this.grupoDAO = new GrupoDAOImpl();
    }

    private void validarGrupo(Grupo grupo) {
        if (grupo == null) {
            throw new RuntimeException("El grupo no puede ser nulo");
        }
        if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (grupo.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
        if (grupo.getCarrera() == null) {
            throw new RuntimeException("La carrera es obligatoria");
        }
    }

    public void agregarGrupo(Grupo grupo) {
        validarGrupo(grupo);
        grupoDAO.agregarGrupo(grupo);
    }

    public void actualizarGrupo(Grupo grupo) {
        validarGrupo(grupo);
        if (grupoDAO.obtenerPorId(grupo.getId()) == null) {
            throw new RuntimeException("El grupo no existe");
        }
        grupoDAO.actualizarGrupo(grupo);
    }

    public void eliminarGrupo(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        grupoDAO.eliminarGrupo(id);
    }

    public Grupo obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return grupoDAO.obtenerPorId(id);
    }

    public List<Grupo> obtenerTodos() {
        return grupoDAO.obtenerTodos();
    }
}