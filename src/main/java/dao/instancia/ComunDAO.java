package dao.instancia;
import modelos.instancia.InstanciaComun;

public interface ComunDAO {
    void agregarComun(InstanciaComun comun);
    void actualizarComun(InstanciaComun comun);
    void eliminarPorInstancia(int idInstancia);
    InstanciaComun obtenerPorInstancia(int idInstancia);
}