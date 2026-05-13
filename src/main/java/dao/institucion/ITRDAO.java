package dao.institucion;

import modelos.institucion.ITR;
import java.util.List;

public interface ITRDAO {
    void agregarITR(ITR itr);
    void actualizarITR(ITR itr);
    void eliminarITR(int id);
    ITR obtenerPorId(int id);
    List<ITR> obtenerTodos();
}
