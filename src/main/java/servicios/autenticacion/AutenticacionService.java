package servicios.autenticacion;

import conexionDB.ConexionBDSingleton;
import modelos.usuario.Rol;
import modelos.usuario.Usuario;
import util.EncriptarContra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AutenticacionService {

    private Connection conexion;

    public AutenticacionService() {
        this.conexion = ConexionBDSingleton.getInstancia().getConexion();
    }

    public Usuario login(String cedula, String contrasena) {

        String sql =
                "SELECT u.cedula, u.nombre, u.apellido, u.email, u.contrasena, u.estado, u.id_rol, r.nom_rol " +
                        "FROM usuarios u " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.cedula = ? AND u.estado = true";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Usuario no encontrado");
            }

            // validar contraseña
            if (!rs.getString("contrasena").equals(EncriptarContra.encriptar(contrasena))){
                throw new RuntimeException("Contraseña incorrecta");
            }

            // construir rol completo
            Rol rol = new Rol(
                    rs.getInt("id_rol"),
                    rs.getString("nom_rol"),
                    true
            );

            // construir usuario
            Usuario u = new Usuario() {};

            u.setCedula(rs.getString("cedula"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setEmail(rs.getString("email"));
            u.setEstado(rs.getBoolean("estado"));
            u.setRol(rol);

            return u;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}