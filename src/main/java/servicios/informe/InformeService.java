package servicios.informe;
import dao.informe.InformeAdjuntoDAO;
import dao.informe.InformeAdjuntoDAOImpl;
import modelos.informe.InformeAdjunto;
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
            throw new RuntimeException("El tipo de informe es obligatorio");
        }
        if (!informe.getTipoArchivo().matches("PDF|JPG|PNG")) {
            throw new RuntimeException("Tipo de informe inválido, debe ser PDF, JPG o PNG");
        }
        if (informe.getRutaArchivo() == null || informe.getRutaArchivo().trim().isEmpty()) {
            throw new RuntimeException("La ruta del informe es obligatoria");
        }
        if (informe.getCategoria() == null || informe.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        if (informe.getCategoria().length() > 50) {
            throw new RuntimeException("La categoría no puede superar los 50 caracteres");
        }
        if (informe.getEstudiante() == null) {
            throw new RuntimeException("El estudiante es obligatorio");
        }
    }

    public void agregarInforme(InformeAdjunto informe) {
        validarInforme(informe);
        informeDAO.agregarInforme(informe);
    }

    public void actualizarInforme(InformeAdjunto informe) {
        validarInforme(informe);
        if (informeDAO.obtenerPorId(informe.getId()) == null) {
            throw new RuntimeException("El informe no existe");
        }
        informeDAO.actualizarInforme(informe);
    }

    public void eliminarInforme(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        InformeAdjunto existente = informeDAO.obtenerPorId(id);
        if (existente == null) {
            throw new RuntimeException("El informe no existe");
        }
        if (!existente.isEstado()) {
            throw new RuntimeException("El informe ya se encuentra dado de baja");
        }
        informeDAO.eliminarInforme(id);
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