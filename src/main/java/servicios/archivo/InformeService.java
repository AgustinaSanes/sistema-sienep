package servicios.archivo;
import dao.archivo.InformeAdjuntoDAO;
import dao.archivo.InformeAdjuntoDAOImpl;
import modelos.archivo.InformeAdjunto;
import java.util.List;

public class InformeService {

    private final InformeAdjuntoDAO informeDAO;

    public InformeService() {
        this.informeDAO = new InformeAdjuntoDAOImpl();
    }

    private void validarInforme(InformeAdjunto informe) {
        if (informe == null) {
            throw new RuntimeException("El informe no puede ser nulo");
        }
        if (informe.getNombre() == null || informe.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (informe.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
        if (informe.getTipoArchivo() == null || informe.getTipoArchivo().trim().isEmpty()) {
            throw new RuntimeException("El tipo de archivo es obligatorio");
        }
        if (!informe.getTipoArchivo().matches("PDF|JPG|PNG")) {
            throw new RuntimeException("Tipo de archivo inválido, debe ser PDF, JPG o PNG");
        }
        if (informe.getRutaArchivo() == null || informe.getRutaArchivo().trim().isEmpty()) {
            throw new RuntimeException("La ruta del archivo es obligatoria");
        }
        if (informe.getEstudiante() == null) {
            throw new RuntimeException("El estudiante es obligatorio");
        }
    }

    public void agregarInforme(InformeAdjunto informe) {
        validarInforme(informe);
        informeDAO.agregarArchivo(informe);
    }

    public void actualizarInforme(InformeAdjunto informe) {
        validarInforme(informe);
        if (informeDAO.obtenerPorId(informe.getId()) == null) {
            throw new RuntimeException("El informe no existe");
        }
        informeDAO.actualizarArchivo(informe);
    }

    public void eliminarInforme(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        if (informeDAO.obtenerPorId(id) == null) {
            throw new RuntimeException("El informe no existe");
        }
        informeDAO.eliminarArchivo(id);
    }

    public InformeAdjunto obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return informeDAO.obtenerPorId(id);
    }

    public List<InformeAdjunto> obtenerTodos() {
        return informeDAO.obtenerTodos();
    }

    public List<InformeAdjunto> obtenerPorEstudiante(String cedula) {
        return informeDAO.obtenerPorEstudiante(cedula);
    }
}