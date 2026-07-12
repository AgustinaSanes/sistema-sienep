package dao.recordatorio;
import conexionDB.ConexionBDSingleton;
import modelos.recordatorio.Frecuencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FrecuenciaDAOImpl implements FrecuenciaDAO {

    private final Connection c;

    public FrecuenciaDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarFrecuencia(Frecuencia frecuencia) {
        String sql = "INSERT INTO frecuencias (descripcion, estado) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, frecuencia.getDescripcion());
            ps.setBoolean(2, frecuencia.isEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificarFrecuencia(Frecuencia frecuencia) {
        String sql = "UPDATE frecuencias SET descripcion = ? WHERE id_frecuencia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, frecuencia.getDescripcion());
            ps.setInt(2, frecuencia.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarFrecuencia(int id) {
        String sql = "UPDATE frecuencias SET estado = FALSE WHERE id_frecuencia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Frecuencia obtenerPorId(int id) {
        String sql = "SELECT * FROM frecuencias WHERE id_frecuencia = ? AND estado = TRUE";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Frecuencia(
                        rs.getInt("id_frecuencia"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Frecuencia> obtenerTodas() {
        List<Frecuencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM frecuencias WHERE estado = TRUE ORDER BY id_frecuencia";

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Frecuencia(
                        rs.getInt("id_frecuencia"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }
}