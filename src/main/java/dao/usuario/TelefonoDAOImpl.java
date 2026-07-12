package dao.usuario;
import conexionDB.ConexionBDSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefonoDAOImpl implements TelefonoDAO {

    private final Connection c;

    public TelefonoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarTelefono(String cedula, String numero) {
        String sql = "INSERT INTO telefonos (cedula, numero) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, numero);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarTelefono(String cedula, String numero) {

        String sql = "DELETE FROM telefonos WHERE cedula = ? AND numero = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, numero);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> obtenerPorEstudiante(String cedula) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT numero FROM telefonos WHERE cedula = ? ORDER BY numero";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("numero"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }
}