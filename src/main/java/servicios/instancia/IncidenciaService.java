package servicios.instancia;
import dao.instancia.IncidenciaDAO;
import dao.instancia.IncidenciaDAOImpl;
import dao.instancia.InvolucradoDAO;
import dao.instancia.InvolucradoDAOImpl;
import modelos.instancia.Incidencia;
import java.util.List;

public class IncidenciaService {

    private final IncidenciaDAO incidenciaDAO;
    private final InvolucradoDAO involucradoDAO;

    public IncidenciaService() {
        this.incidenciaDAO = new IncidenciaDAOImpl();
        this.involucradoDAO = new InvolucradoDAOImpl();
    }

    private void validarIncidencia(Incidencia incidencia) {
        if (incidencia == null) {
            throw new RuntimeException("La incidencia no puede ser nula");
        }
        if (incidencia.getLugar() == null || incidencia.getLugar().trim().isEmpty()) {
            throw new RuntimeException("El lugar es obligatorio");
        }
        if (incidencia.getLugar().length() > 100) {
            throw new RuntimeException("El lugar no puede superar los 100 caracteres");
        }
    }

    public void agregarIncidencia(Incidencia incidencia) {
        validarIncidencia(incidencia);
        incidenciaDAO.agregarIncidencia(incidencia);

        // Guardar involucrados si tiene
        if (incidencia.getInvolucrados() != null) {
            for (String inv : incidencia.getInvolucrados()) {
                involucradoDAO.agregarInvolucrado(incidencia.getId(), inv);
            }
        }
    }

    public void actualizarIncidencia(Incidencia incidencia) {
        validarIncidencia(incidencia);
        if (incidenciaDAO.obtenerPorInstancia(incidencia.getId()) == null) {
            throw new RuntimeException("La incidencia no existe");
        }
        incidenciaDAO.actualizarIncidencia(incidencia);
    }

    public void eliminarIncidencia(int idInstancia) {
        if (idInstancia <= 0) {
            throw new RuntimeException("ID inválido");
        }
        involucradoDAO.eliminarPorIncidencia(idInstancia);
        incidenciaDAO.eliminarPorInstancia(idInstancia);
    }

    public Incidencia obtenerPorInstancia(int idInstancia) {
        if (idInstancia <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return incidenciaDAO.obtenerPorInstancia(idInstancia);
    }

    public List<String> obtenerInvolucrados(int idIncidencia) {
        if (idIncidencia <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return involucradoDAO.obtenerPorIncidencia(idIncidencia);
    }
}