package dao.instancia;
import modelos.instancia.Categoria;
import java.util.List;

public interface CategoriaDAO {
    void agregarCategoria(Categoria categoria);
    void actualizarCategoria(Categoria categoria);
    void eliminarCategoria(int id);
    Categoria obtenerPorId(int id);
    List<Categoria> obtenerTodas();
}

