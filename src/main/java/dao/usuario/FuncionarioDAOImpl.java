package dao.usuario;
import conexionDB.ConexionBDSingleton;
import modelos.usuario.Funcionario;
import modelos.usuario.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAOImpl implements FuncionarioDAO {

    private final Connection c;

    public FuncionarioDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO funcionarios (cedula) VALUES (?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, funcionario.getCedula());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Funcionario obtenerPorCedula(String cedula) {
        String sql = "SELECT u.*, r.nom_rol, r.estado AS estado_rol " +
                "FROM usuarios u " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "JOIN funcionarios f ON u.cedula = f.cedula " +
                "WHERE u.cedula = ? AND u.estado = true";

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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Funcionario> obtenerTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nom_rol, r.estado AS estado_rol " +
                "FROM usuarios u " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "JOIN funcionarios f ON u.cedula = f.cedula " +
                "WHERE u.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
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
                lista.add(funcionario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}