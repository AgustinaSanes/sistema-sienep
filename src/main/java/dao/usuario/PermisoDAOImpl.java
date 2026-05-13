package dao.usuario;
import conexionDB.ConexionBDSingleton;
import modelos.usuario.Permiso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermisoDAOImpl implements PermisoDAO {
    private Connection c;
    public PermisoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarPermiso(Permiso permiso) {
        String sql = "INSERT INTO permisos(descripcion) VALUES(?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, permiso.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actualizarPermiso(Permiso permiso) {
        String sql = "UPDATE permisos SET descripcion=? WHERE id_permiso=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, permiso.getDescripcion());
            ps.setInt(2, permiso.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void eliminarPermiso(int id) {
        String sql = "UPDATE permisos SET estado = false WHERE id_permiso = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Permiso obtenerPorId(int id) {
        String sql = "SELECT * FROM permisos WHERE id_permiso=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Permiso(
                        rs.getInt("id_permiso"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Permiso> obtenerTodos() {
        List<Permiso> permisos = new ArrayList<>();
        String sql = "SELECT * FROM permisos";
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Permiso permiso = new Permiso(
                        rs.getInt("id_permiso"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado")
                );
                permisos.add(permiso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permisos;
    }
}