package servicios.instancia;
import dao.instancia.*;
import modelos.instancia.*;
import util.ValidarCedula;
import java.util.List;

public class InstanciaService {

    private final InstanciaDAO instanciaDAO;
    private final ComunDAO comunDAO;
    private final IncidenciaDAO incidenciaDAO;

    public InstanciaService() {
        this.instanciaDAO = new InstanciaDAOImpl();
        this.comunDAO = new ComunDAOImpl();
        this.incidenciaDAO = new IncidenciaDAOImpl();
    }

    private void validarInstancia(Instancia instancia) {
        if (instancia == null) {
            throw new RuntimeException("La instancia no puede ser nula");
        }
        if (instancia.getTitulo() == null || instancia.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título es obligatorio");
        }
        if (instancia.getTitulo().length() > 100) {
            throw new RuntimeException("El título no puede superar los 100 caracteres");
        }
        if (instancia.getFechaHora() == null) {
            throw new RuntimeException("La fecha y hora son obligatorias");
        }
        if (instancia.getEstudiante() == null) {
            throw new RuntimeException("El estudiante es obligatorio");
        }
        if (instancia.getFuncionario() == null) {
            throw new RuntimeException("El funcionario es obligatorio");
        }
    }

    public void agregarInstancia(Instancia instancia) {
        validarInstancia(instancia);
        instanciaDAO.agregarInstancia(instancia);
    }

    public void actualizarInstancia(Instancia instancia) {
        validarInstancia(instancia);
        instanciaDAO.actualizarInstancia(instancia);

        if (instancia instanceof InstanciaComun comun) {
            comunDAO.actualizarComun(comun);
        } else if (instancia instanceof Incidencia incidencia) {
            incidenciaDAO.actualizarIncidencia(incidencia);
        }
    }

    public void eliminarInstancia(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        instanciaDAO.eliminarInstancia(id);
    }

    public Instancia obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        Instancia instancia = instanciaDAO.obtenerPorId(id);
        if (instancia == null) {
            throw new RuntimeException("Instancia no encontrada");
        }
        return instancia;
    }

    public List<Instancia> obtenerPorEstudiante(String cedula) {
        if (!ValidarCedula.esValida(cedula)) {
            throw new RuntimeException("Cédula inválida");
        }
        return instanciaDAO.obtenerPorEstudiante(cedula);
    }

    public List<Instancia> obtenerPorCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new RuntimeException("ID de categoría inválido");
        }
        return instanciaDAO.obtenerPorCategoria(idCategoria);
    }
}