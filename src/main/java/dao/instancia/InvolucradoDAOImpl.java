package dao.instancia;
import conexionDB.ConexionBDSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvolucradoDAOImpl implements InvolucradoDAO {

    private Connection c;

    public InvolucradoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarInvolucrado(int idIncidencia, String involucrado) {
        String sql = "INSERT INTO involucrados (id_incidencia, involucrado) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idIncidencia);
            ps.setString(2, involucrado);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarPorIncidencia(int idIncidencia) {
        String sql = "DELETE FROM involucrados WHERE id_incidencia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idIncidencia);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> obtenerPorIncidencia(int idIncidencia) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT involucrado FROM involucrados WHERE id_incidencia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idIncidencia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("involucrado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}