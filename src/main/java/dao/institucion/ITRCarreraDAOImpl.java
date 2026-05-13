package dao.institucion;
import conexionDB.ConexionBDSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ITRCarreraDAOImpl implements ITRCarreraDAO {

    private Connection c;

    public ITRCarreraDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarCarreraITR(int idItr, int idCarrera) {
        String sql = "INSERT INTO itr_carreras (id_itr, id_carrera) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idItr);
            ps.setInt(2, idCarrera);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarCarreraITR(int idItr, int idCarrera) {
        String sql = "DELETE FROM itr_carreras WHERE id_itr = ? AND id_carrera = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idItr);
            ps.setInt(2, idCarrera);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> obtenerCarrerasPorITR(int idItr) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_carrera FROM itr_carreras WHERE id_itr = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idItr);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_carrera"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Integer> obtenerITRsPorCarrera(int idCarrera) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_itr FROM itr_carreras WHERE id_carrera = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCarrera);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_itr"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}