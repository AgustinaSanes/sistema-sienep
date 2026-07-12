package dao.usuario;

import modelos.usuario.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void agregarUsuario(Usuario usuario);
    void actualizarUsuario(Usuario usuario);
    void eliminarUsuario(String cedula);
    void activarUsuario(String cedula);
    Usuario buscarPorCedula(String cedula);
    List<Usuario> obtenerTodos();
}