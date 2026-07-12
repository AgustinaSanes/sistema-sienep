package servicios.institucion;
import dao.institucion.CarreraDAO;
import dao.institucion.CarreraDAOImpl;
import modelos.institucion.Carrera;
import java.util.List;

public class CarreraService {

    private final CarreraDAO carreraDAO;

    public CarreraService() {
        this.carreraDAO = new CarreraDAOImpl();
    }

    private void validarCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new RuntimeException("La carrera no puede ser nula");
        }
        if (carrera.getNombre() == null || carrera.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (carrera.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
    }

    public void agregarCarrera(Carrera carrera) {
        validarCarrera(carrera);

        List<Carrera> carreras = carreraDAO.obtenerTodas();
        for (Carrera c : carreras) {
            if (c.getNombre().equalsIgnoreCase(carrera.getNombre())) {
                throw new RuntimeException("Ya existe una carrera con ese nombre");
            }
        }
        carreraDAO.agregarCarrera(carrera);
    }

    public void actualizarCarrera(Carrera carrera) {
        validarCarrera(carrera);

        Carrera existente = carreraDAO.obtenerPorId(carrera.getId());
        if (existente == null) {
            throw new RuntimeException("La carrera no existe");
        }

        for (Carrera c : carreraDAO.obtenerTodas()) {
            if (c.getId() != carrera.getId()
                    && c.getNombre().equalsIgnoreCase(carrera.getNombre())) {
                throw new RuntimeException("Ya existe una carrera con ese nombre");
            }
        }

        carreraDAO.actualizarCarrera(carrera);
    }

    public void eliminarCarrera(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        Carrera existente = carreraDAO.obtenerPorId(id);
        if (existente == null) {
            throw new RuntimeException("La carrera no existe");
        }
        if (!existente.isEstado()) {
            throw new RuntimeException("La carrera ya se encuentra dada de baja");
        }

        carreraDAO.eliminarCarrera(id);
    }

    public Carrera obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return carreraDAO.obtenerPorId(id);
    }

    public List<Carrera> obtenerTodas() {
        return carreraDAO.obtenerTodas();
    }
}