package servicios.instancia;
import dao.instancia.*;
import modelos.instancia.*;
import util.ValidarCedula;

import java.time.LocalDate;
import java.util.List;

public class InstanciaService {

    private final InstanciaDAO instanciaDAO;
    private final ComunDAO comunDAO;
    private final IncidenciaDAO incidenciaDAO;

    public InstanciaService() {
        this.instanciaDAO = new InstanciaDAOImpl();
        this.comunDAO = new ComunDAOImpl();
        this.incidenciaDAO = new IncidenciaDAOImpl();
    }

    private void validarInstancia(Instancia instancia, boolean esCreacion) {
        if (instancia == null) {
            throw new RuntimeException("La instancia no puede ser nula");
        }
        if (instancia.getTitulo() == null || instancia.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título es obligatorio");
        }
        if (instancia.getTitulo().length() > 100) {
            throw new RuntimeException("El título no puede superar los 100 caracteres");
        }
        if (instancia.getComentario() == null || instancia.getComentario().trim().isEmpty()) {
            throw new RuntimeException("El comentario es obligatorio");
        }
        if (instancia.getComentario().length() > 255) {
            throw new RuntimeException("El comentario no puede superar los 255 caracteres");
        }
        if (instancia.getFechaHora() == null) {
            throw new RuntimeException("La fecha y hora son obligatorias");
        }

        if (instancia instanceof Incidencia incidencia) {
            if (incidencia.getLugar() == null || incidencia.getLugar().trim().isEmpty()) {
                throw new RuntimeException("El lugar es obligatorio");
            }

            // La exigencia de tener al menos un involucrado solo aplica al crear.
            // Al actualizar, los involucrados se gestionan por su propio flujo
            // (agregar/eliminar), y no debe bloquear la edición de otros campos.
            if (esCreacion && (incidencia.getInvolucrados() == null || incidencia.getInvolucrados().isEmpty())) {
                throw new RuntimeException("Debe ingresar al menos un involucrado");
            }

            if (incidencia.getInvolucrados() != null) {
                for (String involucrado : incidencia.getInvolucrados()) {
                    if (involucrado == null || involucrado.trim().isEmpty()) {
                        throw new RuntimeException("El involucrado no puede estar vacío");
                    }
                    if (involucrado.length() > 50) {
                        throw new RuntimeException("El involucrado no puede superar los 50 caracteres");
                    }
                }
            }
        }
    }

    public void agregarInstancia(Instancia instancia) {
        validarInstancia(instancia, true);
        instanciaDAO.agregarInstancia(instancia);
    }

    public void actualizarInstancia(Instancia instancia) {
        validarInstancia(instancia, false);
        instanciaDAO.actualizarInstancia(instancia);

        if (instancia instanceof InstanciaComun comun) {
            if (comun.getCategoria() == null) {
                throw new RuntimeException("La categoría es obligatoria");
            }
            comunDAO.actualizarComun(comun);
        } else if (instancia instanceof Incidencia incidencia) {
            incidenciaDAO.actualizarIncidencia(incidencia);
        }
    }

    public void eliminarInstancia(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        instanciaDAO.eliminarInstancia(id);
    }

    public Instancia obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        Instancia instancia = instanciaDAO.obtenerPorId(id);
        if (instancia == null) {
            throw new RuntimeException("Instancia no encontrada");
        }
        return instancia;
    }

    public List<Instancia> obtenerPorEstudiante(String cedula) {
        ValidarCedula.validar(cedula);
        return instanciaDAO.obtenerPorEstudiante(cedula);
    }

    public List<Instancia> obtenerPorCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new RuntimeException("ID de categoría inválido");
        }
        return instanciaDAO.obtenerPorCategoria(idCategoria);
    }

    public List<Instancia> buscarPorFecha(LocalDate fecha) {
        return instanciaDAO.buscarPorFecha(fecha);
    }

    public List<Instancia> buscarPorDescripcion(String descripcion) {
        return instanciaDAO.buscarPorDescripcion(descripcion);
    }
}