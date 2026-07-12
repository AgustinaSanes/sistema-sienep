package dao.usuario;
import conexionDB.ConexionBDSingleton;
import modelos.usuario.Usuario;
import modelos.usuario.Funcionario;
import modelos.usuario.Rol;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection c;

    public UsuarioDAOImpl() {
        this.c = ConexionBDSingleton
                .getInstancia()
                .getConexion();
    }

    //Agregar usuario
    @Override
    public void agregarUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuarios " +
                "(cedula, id_rol, nombre, apellido, email, contrasena, estado) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, usuario.getCedula());
            ps.setInt(2, usuario.getRol().getId());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellido());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getContrasena());
            ps.setBoolean(7, usuario.isEstado());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Actualizar usuario
    @Override
    public void actualizarUsuario(Usuario usuario) {

        String sql = "UPDATE usuarios SET " +
                "id_rol=?, nombre=?, apellido=?, email=?, contrasena=?, estado=? " +
                "WHERE cedula=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuario.getRol().getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getContrasena());
            ps.setBoolean(6, usuario.isEstado());
            ps.setString(7, usuario.getCedula());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Baja lógica
    @Override
    public void eliminarUsuario(String cedula) {

        String sql =
                "UPDATE usuarios SET estado = false WHERE cedula = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Alta lógica (reactivar cuenta)
    @Override
    public void activarUsuario(String cedula) {

        String sql =
                "UPDATE usuarios SET estado = true WHERE cedula = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Obtener por cedula
    @Override
    public Usuario buscarPorCedula(String cedula) {

        String sql =
                "SELECT u.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.cedula = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Funcionario(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getBoolean("estado"),
                        new Rol(
                                rs.getInt("id_rol"),
                                rs.getString("nom_rol"),
                                rs.getBoolean("estado_rol")
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return null;
    }

    //Obtener todos
    @Override
    public List<Usuario> obtenerTodos() {

        List<Usuario> lista = new ArrayList<>();

        String sql =
                "SELECT u.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.estado = true " +
                        "ORDER BY u.apellido, u.nombre";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Usuario usuario = new Funcionario(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getBoolean("estado"),

                        new Rol(
                                rs.getInt("id_rol"),
                                rs.getString("nom_rol"),
                                rs.getBoolean("estado_rol")
                        )
                );

                lista.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }
}