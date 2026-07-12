package dao.institucion;
import conexionDB.ConexionBDSingleton;
import modelos.institucion.Grupo;
import modelos.institucion.Carrera;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAOImpl implements GrupoDAO {

    private final Connection c;

    public GrupoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarGrupo(Grupo grupo) {
        String sql = "INSERT INTO grupos (id_carrera, nom_grupo) VALUES (?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, grupo.getCarrera().getId());
            ps.setString(2, grupo.getNombre());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarGrupo(Grupo grupo) {
        String sql = "UPDATE grupos SET id_carrera = ?, nom_grupo = ? WHERE id_grupo = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, grupo.getCarrera().getId());
            ps.setString(2, grupo.getNombre());
            ps.setInt(3, grupo.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarGrupo(int id) {
        String sql = "UPDATE grupos SET estado = false WHERE id_grupo = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Grupo obtenerPorId(int id) {
        String sql = "SELECT * FROM grupos WHERE id_grupo = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Carrera carrera = new Carrera();
                carrera.setId(rs.getInt("id_carrera"));

                return new Grupo(
                        rs.getInt("id_grupo"),
                        rs.getString("nom_grupo"),
                        rs.getBoolean("estado"),
                        carrera
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM grupos ORDER BY id_grupo";

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Carrera carrera = new Carrera();
                carrera.setId(rs.getInt("id_carrera"));

                Grupo grupo = new Grupo(
                        rs.getInt("id_grupo"),
                        rs.getString("nom_grupo"),
                        rs.getBoolean("estado"),
                        carrera
                );

                lista.add(grupo);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }
}