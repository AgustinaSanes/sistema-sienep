package dao.usuario;
import conexionDB.ConexionBDSingleton;
import modelos.usuario.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAOImpl implements RolDAO {
    private Connection c;
    public RolDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarRol(Rol rol) {
        String sql = "INSERT INTO roles (nom_rol) VALUES (?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarRol(Rol rol) {
        String sql = "UPDATE roles SET nom_rol = ? WHERE id_rol = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setInt(2, rol.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarRol(int id) {

        String sql = "UPDATE roles SET estado = false WHERE id_rol = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rol obtenerPorId(int id) {
        String sql = "SELECT * FROM roles WHERE id_rol = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nom_rol"),
                        rs.getBoolean("estado")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rol> obtenerTodos() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nom_rol"),
                        rs.getBoolean("estado")
                );
                lista.add(rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}