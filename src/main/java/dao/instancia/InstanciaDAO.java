package dao.instancia;

import modelos.instancia.Instancia;

import java.time.LocalDate;
import java.util.List;

public interface InstanciaDAO {
    void agregarInstancia(Instancia instancia);
    void actualizarInstancia(Instancia instancia);
    void eliminarInstancia(int id);
    Instancia obtenerPorId(int id);
    List<Instancia> obtenerPorEstudiante(String cedula);
    List<Instancia> obtenerPorCategoria(int idCategoria);
    List<Instancia> buscarPorFecha(LocalDate fecha);
    List<Instancia> buscarPorDescripcion(String descripcion);
}
