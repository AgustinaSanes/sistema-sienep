package dao.instancia;
import conexionDB.ConexionBDSingleton;
import modelos.instancia.*;
import java.sql.*;

public class IncidenciaDAOImpl implements IncidenciaDAO {

    private final Connection c;

    public IncidenciaDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarIncidencia(Incidencia inc) {

        String sql = "INSERT INTO incidencias (id_instancia, lugar) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, inc.getId());
            ps.setString(2, inc.getLugar());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarIncidencia(Incidencia inc) {

        String sql = "UPDATE incidencias SET lugar = ? WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, inc.getLugar());
            ps.setInt(2, inc.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Incidencia obtenerPorInstancia(int idInstancia) {
        String sql = "SELECT * FROM incidencias WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Incidencia(
                        idInstancia, false, null, null,
                        null, true, null, null,
                        rs.getString("lugar")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return null;
    }
}