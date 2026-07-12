package servicios.instancia;
import dao.instancia.CategoriaDAO;
import dao.instancia.CategoriaDAOImpl;
import dao.instancia.InstanciaDAO;
import dao.instancia.InstanciaDAOImpl;
import modelos.instancia.Categoria;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;
    private final InstanciaDAO instanciaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAOImpl();
        this.instanciaDAO = new InstanciaDAOImpl();
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new RuntimeException("La categoría no puede ser nula");
        }
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (categoria.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
    }

    public void agregarCategoria(Categoria categoria) {
        validarCategoria(categoria);
        List<Categoria> categorias = categoriaDAO.obtenerTodas();
        for (Categoria c : categorias) {
            if (c.getNombre().equalsIgnoreCase(categoria.getNombre())) {
                throw new RuntimeException("Ya existe una categoría con ese nombre");
            }
        }
        categoriaDAO.agregarCategoria(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        validarCategoria(categoria);
        if (categoriaDAO.obtenerPorId(categoria.getId()) == null) {
            throw new RuntimeException("La categoría no existe");
        }
        categoriaDAO.actualizarCategoria(categoria);
    }

    public void eliminarCategoria(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        if (categoriaDAO.obtenerPorId(id) == null) {
            throw new RuntimeException("La categoría no existe");
        }
        if (!instanciaDAO.obtenerPorCategoria(id).isEmpty()) {
            throw new RuntimeException("La categoría se encuentra en uso y no puede darse de baja");
        }
        categoriaDAO.eliminarCategoria(id);
    }

    public Categoria obtenerPorId(int id) {
        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        return categoriaDAO.obtenerPorId(id);
    }

    public List<Categoria> obtenerTodas() {
        return categoriaDAO.obtenerTodas();
    }
}