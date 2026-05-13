package dao.institucion;

import modelos.institucion.Carrera;
import java.util.List;

public interface CarreraDAO {
    void agregarCarrera(Carrera carrera);
    void actualizarCarrera(Carrera carrera);
    void eliminarCarrera(int id);
    Carrera obtenerPorId(int id);
    List<Carrera> obtenerTodas();
}
