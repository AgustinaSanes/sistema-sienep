package servicios.usuario;

import dao.usuario.EstudianteDAO;
import dao.usuario.EstudianteDAOImpl;
import modelos.usuario.Estudiante;
import util.ValidarCedula;
import util.ValidarEdad;
import util.ValidarTelefono;

import java.util.List;

public class EstudianteService {

    private final EstudianteDAO estudianteDAO;

    public EstudianteService() {
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    //Validar estudiante
    private void validarEstudiante(Estudiante estudiante) {

        if (estudiante == null) {
            throw new RuntimeException("El estudiante no puede ser nulo");
        }

        // Cédula
        ValidarCedula.validar(estudiante.getCedula());

        // Sistema salud
        if (estudiante.getSistemaSalud() == null ||
                estudiante.getSistemaSalud().trim().isEmpty()) {
            throw new RuntimeException("El sistema de salud obligatorio");
        }
        if (estudiante.getSistemaSalud().length() > 50) {
            throw new RuntimeException("El sistema de salud no debe superar los 50 caracteres");
        }

        // Fecha nacimiento
        if (estudiante.getFechaNacimiento() == null) {
            throw new RuntimeException("La fecha de nacimiento obligatoria");
        }

        if (!ValidarEdad.esMayorDeEdad(estudiante.getFechaNacimiento())) {
            throw new RuntimeException("El estudiante debe ser mayor de edad");
        }
        // Motivo
        if (estudiante.getMotivo() == null ||
                estudiante.getMotivo().trim().isEmpty()) {
            throw new RuntimeException("El motivo es obligatorio");
        }
        if (estudiante.getMotivo().length() > 250) {
            throw new RuntimeException("El motivo no debe superar los 250 caracteres");
        }
        // Foto
        if (estudiante.getFoto() != null &&
                estudiante.getFoto().length() > 250) {
            throw new RuntimeException("La ruta de foto no puede superar los 250 caracteres");
        }
        // Observaciones
        if (estudiante.getObsComentarios() != null &&
                estudiante.getObsComentarios().length() > 250) {
            throw new RuntimeException("El comentario no puede superar los 250 caracteres");
        }
        // Info salud
        if (estudiante.getInfoEstadoSalud() != null &&
                estudiante.getInfoEstadoSalud().length() > 250) {
            throw new RuntimeException("La información de salud no puede superar los 250 caracteres");
        }
        // Dirección
        if (estudiante.getCalle() != null &&
                estudiante.getCalle().length() > 50) {
            throw new RuntimeException("La calle no puede superar los 50 caracteres");
        }
        if (estudiante.getNroPuerta() != null &&
                estudiante.getNroPuerta().length() > 50) {
            throw new RuntimeException("Número de puerta no puede superar los 50 caracteres");
        }
        if (estudiante.getTelefono() == null || estudiante.getTelefono().isEmpty()) {
            throw new RuntimeException("Debe ingresar al menos un teléfono");
        }

        for (String telefono : estudiante.getTelefono()) {
            ValidarTelefono.validar(telefono);
        }
    }

    //Agregar estudiante
    public void agregarEstudiante(Estudiante e) {

        validarEstudiante(e);

        if (estudianteDAO.obtenerPorCedula(e.getCedula()) != null) {
            throw new RuntimeException("El estudiante ya existe");
        }

        estudianteDAO.agregarEstudiante(e);
    }

    //Actualizar estudiante
    public void actualizarEstudiante(Estudiante e) {

        validarEstudiante(e);

        Estudiante existente =
                estudianteDAO.obtenerPorCedula(e.getCedula());

        if (existente == null) {
            throw new RuntimeException("El estudiante no existe");
        }

        estudianteDAO.actualizarEstudiante(e);
    }

    //Obtener estudiante por cedula
    public Estudiante obtenerPorCedula(String cedula) {

        ValidarCedula.validar(cedula);

        return estudianteDAO.obtenerPorCedula(cedula);
    }

    //Buscar por nombre o apellido
    public List<Estudiante> buscarPorNombreApellido(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("Texto vacío");
        }

        return estudianteDAO.buscarPorNombreApellido(texto);
    }

    // Buscar por carrera
    public List<Estudiante> buscarPorCarrera(int idCarrera) {

        if (idCarrera <= 0) {
            throw new RuntimeException("ID de carrera inválido");
        }

        return estudianteDAO.buscarPorCarrera(idCarrera);
    }

    // Buscar por grupo
    public List<Estudiante> buscarPorGrupo(int idGrupo) {

        if (idGrupo <= 0) {
            throw new RuntimeException("ID de grupo inválido");
        }

        return estudianteDAO.buscarPorGrupo(idGrupo);
    }

    // Buscar por estado
    public List<Estudiante> buscarPorEstado(boolean estado) {
        return estudianteDAO.buscarPorEstado(estado);
    }
}