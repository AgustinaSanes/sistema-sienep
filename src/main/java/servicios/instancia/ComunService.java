package servicios.instancia;
import dao.instancia.ComunDAO;
import dao.instancia.ComunDAOImpl;
import modelos.instancia.InstanciaComun;

public class ComunService {

    private final ComunDAO comunDAO;

    public ComunService() {
        this.comunDAO = new ComunDAOImpl();
    }

    private void validarComun(InstanciaComun comun) {
        if (comun == null) {
            throw new RuntimeException("La instancia común no puede ser nula");
        }
        if (comun.getCategoria() == null) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        if (comun.getCategoria().getId() <= 0) {
            throw new RuntimeException("Categoría inválida");
        }
    }

    public void agregarComun(InstanciaComun comun) {
        validarComun(comun);
        comunDAO.agregarComun(comun);
    }

    public void actualizarComun(InstanciaComun comun) {
        validarComun(comun);
        if (comunDAO.obtenerPorInstancia(comun.getId()) == null) {
            throw new RuntimeException("La instancia común no existe");
        }
        comunDAO.actualizarComun(comun);
    }
}