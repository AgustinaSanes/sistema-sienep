package dao.instancia;
import conexionDB.ConexionBDSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvolucradoDAOImpl implements InvolucradoDAO {

    private final Connection c;

    public InvolucradoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarInvolucrado(int idInstancia, String involucrado) {
        String sql = "INSERT INTO involucrados (id_instancia, involucrado) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ps.setString(2, involucrado);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarInvolucrado(int idInstancia, String involucrado) {

        String sql = "DELETE FROM involucrados WHERE id_instancia = ? AND involucrado = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ps.setString(2, involucrado);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarPorIncidencia(int idInstancia) {
        String sql = "DELETE FROM involucrados WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> obtenerPorIncidencia(int idInstancia) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT involucrado FROM involucrados WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
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