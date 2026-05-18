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
        String sql = "INSERT INTO frecuencias (descripcion) VALUES (?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, frecuencia.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Frecuencia obtenerPorId(int id) {
        String sql = "SELECT * FROM frecuencias WHERE id_frecuencia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Frecuencia(
                        rs.getInt("id_frecuencia"),
                        rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Frecuencia> obtenerTodas() {
        List<Frecuencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM frecuencias";

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Frecuencia(
                        rs.getInt("id_frecuencia"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}