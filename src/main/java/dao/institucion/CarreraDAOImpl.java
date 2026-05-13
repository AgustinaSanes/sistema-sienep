package dao.institucion;
import conexionDB.ConexionBDSingleton;
import modelos.institucion.Carrera;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CarreraDAOImpl implements CarreraDAO {
    private Connection c;

    public CarreraDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarCarrera(Carrera carrera) {
        String sql = "INSERT INTO carreras (nom_carrera) VALUES (?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, carrera.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarCarrera(Carrera carrera) {
        String sql = "UPDATE carreras SET nom_carrera = ? WHERE id_carrera = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, carrera.getNombre());
            ps.setInt(2, carrera.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarCarrera(int id) {
        String sql = "UPDATE carreras SET estado = false WHERE id_carrera = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Carrera obtenerPorId(int id) {
        String sql = "SELECT * FROM carreras WHERE id_carrera = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Carrera(
                        rs.getInt("id_carrera"),
                        rs.getString("nom_carrera"),
                        rs.getBoolean("estado")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Carrera> obtenerTodas() {
        List<Carrera> lista = new ArrayList<>();
        String sql = "SELECT * FROM carreras";
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Carrera carrera = new Carrera(
                        rs.getInt("id_carrera"),
                        rs.getString("nom_carrera"),
                        rs.getBoolean("estado")
                );
                lista.add(carrera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}