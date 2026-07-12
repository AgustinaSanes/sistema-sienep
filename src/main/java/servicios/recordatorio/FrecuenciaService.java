package servicios.recordatorio;

import dao.recordatorio.FrecuenciaDAO;
import dao.recordatorio.FrecuenciaDAOImpl;
import modelos.recordatorio.Frecuencia;

import java.util.List;

public class FrecuenciaService {

    private final FrecuenciaDAO frecuenciaDAO = new FrecuenciaDAOImpl();


    public void agregarFrecuencia(Frecuencia frecuencia) {

        validarFrecuencia(frecuencia);

        frecuenciaDAO.agregarFrecuencia(frecuencia);
    }

    public void modificarFrecuencia(Frecuencia frecuencia) {

        validarFrecuencia(frecuencia);

        frecuenciaDAO.modificarFrecuencia(frecuencia);
    }


    public void eliminarFrecuencia(int id) {

        Frecuencia frecuencia = frecuenciaDAO.obtenerPorId(id);

        if (frecuencia == null) {
            throw new RuntimeException("La frecuencia no existe.");
        }

        if (!frecuencia.isEstado()) {
            throw new RuntimeException("La frecuencia ya está dada de baja.");
        }

        frecuenciaDAO.eliminarFrecuencia(id);
    }


    public Frecuencia obtenerPorId(int id) {
        if (id <= 0) throw new RuntimeException("ID inválido");
        return frecuenciaDAO.obtenerPorId(id);
    }


    public List<Frecuencia> obtenerTodas() {
        return frecuenciaDAO.obtenerTodas();
    }


    private void validarFrecuencia(Frecuencia frecuencia) {

        if (frecuencia == null) {
            throw new RuntimeException("La frecuencia no puede ser nula.");
        }

        if (frecuencia.getDescripcion() == null ||
                frecuencia.getDescripcion().trim().isEmpty()) {

            throw new RuntimeException("La descripción de la frecuencia es obligatoria.");
        }


        if (frecuencia.getDescripcion().length() > 50) {
            throw new RuntimeException("La descripción no puede superar los 50 caracteres.");
        }
    }
}