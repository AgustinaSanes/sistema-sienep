package dao.usuario;
import conexionDB.ConexionBDSingleton;
import modelos.usuario.Permiso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolPermisoDAOImpl implements RolPermisoDAO {

    private final Connection c;

    public RolPermisoDAOImpl() {
        this.c = ConexionBDSingleton
                .getInstancia()
                .getConexion();
    }

    //Asignar permiso a rol
    @Override
    public void asignarPermiso(int idRol, int idPermiso) {

        String sql =
                "INSERT INTO rol_permisos " +
                        "(id_rol, id_permiso) " +
                        "VALUES (?, ?)";

        try (PreparedStatement ps =
                     c.prepareStatement(sql)) {

            ps.setInt(1, idRol);
            ps.setInt(2, idPermiso);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Quitar permiso de rol
    @Override
    public void quitarPermiso(int idRol, int idPermiso) {

        String sql =
                "DELETE FROM rol_permisos " +
                        "WHERE id_rol = ? " +
                        "AND id_permiso = ?";

        try (PreparedStatement ps =
                     c.prepareStatement(sql)) {

            ps.setInt(1, idRol);
            ps.setInt(2, idPermiso);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    //Obtener permisos por rol
    @Override
    public List<Permiso> obtenerPermisosRol(int idRol) {

        List<Permiso> permisos =
                new ArrayList<>();

        String sql =
                "SELECT p.* " +
                        "FROM permisos p " +
                        "JOIN rol_permisos rp " +
                        "ON p.id_permiso = rp.id_permiso " +
                        "WHERE rp.id_rol = ? " +
                        "AND p.estado = true " +
                        "ORDER BY p.descripcion";

        try (PreparedStatement ps =
                     c.prepareStatement(sql)) {

            ps.setInt(1, idRol);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Permiso permiso =
                        new Permiso(
                                rs.getInt("id_permiso"),
                                rs.getString("descripcion"),
                                rs.getBoolean("estado")
                        );

                permisos.add(permiso);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return permisos;
    }
}

