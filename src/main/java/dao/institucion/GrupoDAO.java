package dao.institucion;

import modelos.institucion.Grupo;
import java.util.List;

public interface GrupoDAO {
    void agregarGrupo(Grupo grupo);
    void actualizarGrupo(Grupo grupo);
    void eliminarGrupo(int id);
    Grupo obtenerPorId(int id);
    List<Grupo> obtenerTodos();
}
