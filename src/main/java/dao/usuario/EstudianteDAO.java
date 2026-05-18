package dao.usuario;

import modelos.usuario.Estudiante;
import java.util.List;

public interface EstudianteDAO {
    void agregarEstudiante(Estudiante estudiante);
    void actualizarEstudiante(Estudiante estudiante);
    Estudiante obtenerPorCedula(String cedula);
    List<Estudiante> buscarPorNombreApellido(String texto);
    List<Estudiante> buscarPorCarrera(int idCarrera);
    List<Estudiante> buscarPorGrupo(int idGrupo);
    List<Estudiante> buscarPorEstado(boolean estado);
}